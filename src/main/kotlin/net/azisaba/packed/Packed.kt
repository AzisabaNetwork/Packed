package net.azisaba.packed

import kotlinx.serialization.json.Json
import net.azisaba.packed.equipment.PackEquipmentModel
import net.azisaba.packed.font.PackFont
import net.azisaba.packed.items.PackItemModel
import net.azisaba.packed.models.PackModel
import net.azisaba.packed.sounds.PackSoundEvent
import net.kyori.adventure.key.Key
import org.bukkit.plugin.Plugin
import java.net.URI
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.io.path.writeText

data class Packed(
    val metadata: PackMetadata,
    val resourceMap: Map<Type<*>, Set<*>> = emptyMap(),
) {
    @Suppress("UNCHECKED_CAST")
    fun <R : Any> resourceSet(type: Type<R>): Set<R> = resourceMap[type] as? Set<R> ?: emptySet()

    companion object BuiltinTypes {
        val EQUIPMENT_MODEL: Type<IdentifiedResource<PackEquipmentModel>> = jsonPerFileType("equipment")
        val FONT: Type<IdentifiedResource<PackFont>> = jsonPerFileType("font")
        val ITEM_MODEL: Type<IdentifiedResource<PackItemModel>> = jsonPerFileType("items")
        val MODEL: Type<IdentifiedResource<PackModel>> = jsonPerFileType("models")
        val SOUND_EVENT: Type<IdentifiedResource<PackSoundEvent>> = Type(::buildSoundsJson)
        val PLUGIN_RESOURCES: Type<Plugin> = Type(::buildPluginResources)

        private inline fun <reified R : IdentifiedResource<*>> jsonPerFileType(pathPrefix: String): Type<R> =
            Type { json, pathResolver, resourceSet ->
                resourceSet.associateBy { it.key }.forEach { (key, value) ->
                    val path = pathResolver.filePath(key, pathPrefix, ".json")
                    val jsonString = json.encodeToString(value)
                    path.writeText(jsonString)
                }
            }

        private fun buildSoundsJson(json: Json, pathResolver: PackPathResolver, resourceSet: Set<IdentifiedResource<PackSoundEvent>>) {
            val soundEventsByNamespace =
                resourceSet.associate { it.key to it.value }.entries.groupBy { it.key.namespace() }
            for ((namespace, soundEvents) in soundEventsByNamespace) {
                val soundEventMap = soundEvents.associate { it.key.value() to it.value }
                val jsonString = json.encodeToString(soundEventMap)
                val jsonFile = pathResolver.filePath(Key.key(namespace, "sounds"), extension = ".json")
                jsonFile.writeText(jsonString)
            }
        }

        private fun buildPluginResources(json: Json, pathResolver: PackPathResolver, resourceSet: Set<Plugin>) {
            for (plugin in resourceSet) {
                val javaClass = plugin.javaClass
                val jarUri = javaClass.protectionDomain.codeSource.location.toURI()
                val jarFsUri = URI.create("jar:$jarUri")

                FileSystems.newFileSystem(jarFsUri, emptyMap<String, Any>()).use { fs ->
                    val assetsPath = fs.getPath("/assets")
                    if (!assetsPath.exists()) return@use

                    Files.walk(assetsPath).forEach { stream ->
                        stream.forEach { assetPath ->
                            val outputPath = pathResolver.assetsPath.resolve(assetsPath.relativize(assetPath))
                            if (assetPath.isDirectory()) {
                                outputPath.createDirectories()
                            } else {
                                outputPath.parent?.createDirectories()
                                Files.copy(stream, outputPath, StandardCopyOption.REPLACE_EXISTING)
                            }
                        }
                    }
                }
            }
        }
    }

    fun interface Type<R : Any> {
        fun build(json: Json, pathResolver: PackPathResolver, resourceSet: Set<R>)
    }
}
