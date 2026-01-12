package com.tksimeji.packed

import com.tksimeji.packed.equipment.PackEquipmentModel
import com.tksimeji.packed.font.PackFont
import com.tksimeji.packed.items.PackItemModel
import com.tksimeji.packed.models.PackModel
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.ClassDiscriminatorMode
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Namespaced
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile
import kotlin.io.path.exists
import kotlin.io.path.writeText

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
}

private class PathResolver(rootPath: Path) {
    private val assetsPath: Path by lazy {
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

    fun namespacePath(namespace: Namespaced): Path {
        val path = assetsPath.resolve(namespace.namespace())
        return if (!path.exists()) {
            path.createDirectories()
        } else path
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
}

private inline fun <reified T : Any> createJsonAssets(pathResolver: PathResolver, pathPrefix: String, map: Map<Key, T>) {
    map.forEach { (key, value) ->
        val path = pathResolver.filePath(key, pathPrefix, ".json")
        val jsonString = json.encodeToString(value)
        path.writeText(jsonString)
    }
}
