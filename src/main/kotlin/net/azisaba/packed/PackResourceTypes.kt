package net.azisaba.packed

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import net.azisaba.packed.equipment.EquipmentModelResourceType
import net.azisaba.packed.equipment.PackEquipmentModel
import net.azisaba.packed.font.PackFont
import net.azisaba.packed.items.PackItemModel
import net.azisaba.packed.models.PackModel
import net.azisaba.packed.sounds.PackSoundEvent
import net.azisaba.packed.util.KeyedPackResource
import net.azisaba.packed.util.PackPathResolver
import org.bukkit.plugin.Plugin
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.jar.JarFile
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.io.path.writeText
import kotlin.sequences.forEach
import kotlin.use

object PackResourceTypes {
    val EQUIPMENT_MODEL: PackResourceType<KeyedPackResource<PackEquipmentModel>> = EquipmentModelResourceType
    val FONT: PackResourceType<KeyedPackResource<PackFont>> = PackFont.resourceType()
    val ITEM_MODEL: PackResourceType<KeyedPackResource<PackItemModel>> = PackItemModel.resourceType()
    val MODEL: PackResourceType<KeyedPackResource<PackModel>> = PackModel.resourceType()
    val SOUND_EVENT: PackResourceType<KeyedPackResource<PackSoundEvent>> = PackSoundEvent.resourceType()
    val PLUGIN_RESOURCE: PackResourceType<Plugin> = PluginResourceType
}

internal open class KeyedJsonPerFileResourceType<T : Any>(
    private val pathPrefix: String,
    private val kSerializer: KSerializer<T>
) : PackResourceType<KeyedPackResource<T>> {
    override fun build(json: Json, pathResolver: PackPathResolver, resourceSet: Set<KeyedPackResource<T>>) {
        resourceSet.associateBy { it.key }.forEach { (key, resource) ->
            val path = pathResolver.filePath(key, pathPrefix, ".json")
            val jsonString = json.encodeToString(kSerializer, resource.value)
            path.writeText(jsonString)
        }
    }
}

private object PluginResourceType : PackResourceType<Plugin> {
    override fun build(json: Json, pathResolver: PackPathResolver, resourceSet: Set<Plugin>) {
        for (plugin in resourceSet) {
            val sourcePath = Path.of(plugin.javaClass.protectionDomain.codeSource.location.toURI())
            if (sourcePath.isDirectory()) {
                copyAssetsFromDirectory(sourcePath, pathResolver.assetsPath)
            } else {
                copyAssetsFromJar(sourcePath, pathResolver.assetsPath)
            }
        }
    }

    private fun copyAssetsFromDirectory(sourceRoot: Path, outputAssetsPath: Path) {
        val assetsPath = sourceRoot.resolve("assets")
        if (!assetsPath.exists()) return

        Files.walk(assetsPath).use { stream ->
            stream.forEach { assetPath ->
                val outputPath = outputAssetsPath.resolve(assetsPath.relativize(assetPath).toString())
                if (Files.isDirectory(assetPath)) {
                    outputPath.createDirectories()
                } else {
                    outputPath.parent?.createDirectories()
                    Files.copy(assetPath, outputPath, StandardCopyOption.REPLACE_EXISTING)
                }
            }
        }
    }

    private fun copyAssetsFromJar(jarPath: Path, outputAssetsPath: Path) {
        JarFile(jarPath.toFile()).use { jar ->
            jar.entries().asSequence()
                .filter { it.name.startsWith("assets/") }
                .forEach { entry ->
                    val relativePath = entry.name.removePrefix("assets/")
                    val outputPath = outputAssetsPath.resolve(relativePath)
                    if (entry.isDirectory) {
                        outputPath.createDirectories()
                    } else {
                        outputPath.parent?.createDirectories()
                        jar.getInputStream(entry).use { input ->
                            Files.copy(input, outputPath, StandardCopyOption.REPLACE_EXISTING)
                        }
                    }
                }
        }
    }
}
