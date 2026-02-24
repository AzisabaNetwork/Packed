package net.azisaba.packed

import net.azisaba.packed.equipment.PackEquipmentModel
import net.azisaba.packed.font.PackFont
import net.azisaba.packed.items.PackItemModel
import net.azisaba.packed.models.PackModel
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.ClassDiscriminatorMode
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import net.azisaba.packed.sounds.PackSoundEvent
import net.azisaba.packed.sounds.createSoundsJson
import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Namespaced
import java.net.URI
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.io.path.writeText
import kotlin.reflect.KClass

@OptIn(ExperimentalSerializationApi::class)
private val json: Json = Json {
    prettyPrint = true
    namingStrategy = JsonNamingStrategy.SnakeCase
    classDiscriminatorMode = ClassDiscriminatorMode.NONE
}

@DslMarker
annotation class BuilderDsl

@BuilderDsl
class PackBuilder internal constructor() {
    internal var equipmentModelMap: Map<Key, PackEquipmentModel> = emptyMap()
    internal var fontMap: Map<Key, PackFont> = emptyMap()
    internal var itemModelMap: Map<Key, PackItemModel> = emptyMap()
    internal var modelMap: Map<Key, PackModel> = emptyMap()
    internal var soundMap: Map<Key, PackSoundEvent> = emptyMap()

    internal val includedResources: MutableSet<KClass<*>> = mutableSetOf()

    fun equipment(map: () -> Map<Key, PackEquipmentModel>) {
        equipmentModelMap = map().toMap()
    }

    fun font(map: () -> Map<Key, PackFont>) {
        fontMap = map().toMap()
    }

    fun items(map: () -> Map<Key, PackItemModel>) {
        itemModelMap = map().toMap()
    }

    fun models(map: () -> Map<Key, PackModel>) {
        modelMap = map().toMap()
    }

    fun sounds(map: () -> Map<Key, PackSoundEvent>) {
        soundMap = map().toMap()
    }

    fun includeResources(kClass: KClass<*>) {
        includedResources += kClass
    }
}

internal class PathResolver(rootPath: Path) {
    val assetsPath: Path by lazy {
        val path = rootPath.resolve("assets")
        if (!path.exists()) {
            path.createDirectories()
        }
        path
    }

    fun filePath(key: Key, prefix: String = "", extension: String = ""): Path =
        resolveNamespacedPath(key, "$prefix/${key.value()}$extension").createFileIfNotExists()

    fun dirPath(key: Key, prefix: String = ""): Path =
        resolveNamespacedPath(key, "$prefix/${key.value()}").createDirectories()

    fun namespacePath(namespace: Namespaced): Path = namespacePath(namespace.namespace())

    fun namespacePath(namespace: String): Path {
        val path = assetsPath.resolve(namespace)
        return if (!path.exists()) path.createDirectories() else path
    }

    private fun resolveNamespacedPath(namespace: Namespaced, relativePath: String): Path {
        val namespacePath = namespacePath(namespace)
        return namespacePath.resolve(relativePath)
    }

    private fun Path.createFileIfNotExists(): Path {
        parent.createDirectories()
        return if (!exists()) createFile() else this
    }
}

fun packed(output: Path, block: PackBuilder.() -> Unit) {
    val pathResolver = PathResolver(output)
    val builder = PackBuilder().apply(block)

    createJsonAssets(pathResolver, "equipment", builder.equipmentModelMap)
    createJsonAssets(pathResolver, "font", builder.fontMap)
    createJsonAssets(pathResolver, "items", builder.itemModelMap)
    createJsonAssets(pathResolver, "models", builder.modelMap)

    createSoundsJson(pathResolver, json, builder.soundMap)

    builder.includedResources.forEach { copyResources(pathResolver, it) }
}

private inline fun <reified T : Any> createJsonAssets(pathResolver: PathResolver, pathPrefix: String, map: Map<Key, T>) {
    map.forEach { (key, value) ->
        val path = pathResolver.filePath(key, pathPrefix, ".json")
        val jsonString = json.encodeToString(value)
        path.writeText(jsonString)
    }
}

private fun copyResources(pathResolver: PathResolver, kClass: KClass<*>) {
    val javaClass = kClass.java
    val jarUri = javaClass.protectionDomain.codeSource.location.toURI()
    val jarFsUri = URI.create("jar:$jarUri")

    FileSystems.newFileSystem(jarFsUri, emptyMap<String, Any>()).use { fs ->
        val assetsPath = fs.getPath("/assets")
        if (!assetsPath.exists()) return@use

        Files.walk(assetsPath).forEach { assetPath ->
            val outputPath = pathResolver.assetsPath.resolve(assetsPath.relativize(assetPath))
            if (assetPath.isDirectory()) {
                assetPath.createDirectories()
            } else {
                Files.copy(assetPath, outputPath, StandardCopyOption.REPLACE_EXISTING)
            }
        }
    }
}
