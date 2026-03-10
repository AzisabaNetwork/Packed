package net.azisaba.packed

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import net.azisaba.packed.equipment.PackEquipmentLayer
import net.azisaba.packed.equipment.PackEquipmentLayerKey
import net.azisaba.packed.equipment.PackEquipmentModel
import net.azisaba.packed.font.PackFont
import net.azisaba.packed.items.PackItemModel
import net.azisaba.packed.lang.PackLanguage
import net.azisaba.packed.lang.Translation
import net.azisaba.packed.models.PackModel
import net.azisaba.packed.sounds.PackSoundEvent
import kotlin.io.path.createDirectories
import kotlin.io.path.writeText

typealias PackedEntries<T> = Map<PackedKey<T>, T>

abstract class PackedComponent<T : Any>(val entries: PackedEntries<T>) : PackedExportable {
    companion object {
        fun equipmentModel(entries: PackedEntries<PackEquipmentModel>): PackedComponent<PackEquipmentModel> =
            JsonFilePerEntry(
                entries,
                MapSerializer(PackEquipmentLayerKey.serializer(), PackEquipmentLayer.serializer()),
                "equipment",
            )

        fun font(entries: PackedEntries<PackFont>): PackedComponent<PackFont> = JsonFilePerEntry(
            entries,
            PackFont.serializer(),
            "font",
        )

        fun itemModel(entries: PackedEntries<PackItemModel>): PackedComponent<PackItemModel> = JsonFilePerEntry(
            entries,
            PackItemModel.serializer(),
            "items",
        )

        fun language(entries: PackedEntries<PackLanguage>): PackedComponent<PackLanguage> = JsonFilePerEntry(
            entries,
            MapSerializer(String.serializer(), Translation.serializer()),
            "lang",
        )

        fun model(entries: PackedEntries<PackModel>): PackedComponent<PackModel> = JsonFilePerEntry(
            entries,
            PackModel.serializer(),
            "models",
        )

        fun soundEvent(entries: PackedEntries<PackSoundEvent>): PackedComponent<PackSoundEvent> = JsonFilePerNamespace(
            entries,
            PackSoundEvent.serializer(),
            "sounds.json",
        )
    }

    class JsonFilePerEntry<T : Any>(
        entries: PackedEntries<T>,
        val kSerializer: KSerializer<T>,
        val pathPrefix: String,
    ) : PackedComponent<T>(entries) {
        override fun export(context: PackedExportContext) {
            for ((key, value) in entries) {
                val filePath = context.pathResolver.filePath(key, pathPrefix, extension = ".json")

                filePath.parent.createDirectories()

                val jsonString = context.json.encodeToString(kSerializer, value)

                filePath.writeText(jsonString)
            }
        }
    }

    class JsonFilePerNamespace<T : Any>(
        entries: PackedEntries<T>,
        kSerializer: KSerializer<T>,
        val fileName: String,
    ) : PackedComponent<T>(entries) {
        private val mapSerializer: KSerializer<Map<String, T>> = MapSerializer(String.serializer(), kSerializer)

        override fun export(context: PackedExportContext) {
            val grouped = buildMap {
                for ((key, value) in this@JsonFilePerNamespace.entries) {
                    val namespaceMap = getOrPut(key.namespace()) { mutableMapOf() }
                    namespaceMap[key.value()] = value
                }
            }

            for ((namespace, namespaceEntries) in grouped) {
                val filePath = context.pathResolver
                    .namespacePath(namespace)
                    .resolve(fileName)

                filePath.parent.createDirectories()

                val jsonString = context.json.encodeToString(mapSerializer, namespaceEntries)

                filePath.writeText(jsonString)
            }
        }
    }
}
