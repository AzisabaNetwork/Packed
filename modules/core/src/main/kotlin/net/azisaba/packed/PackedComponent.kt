package net.azisaba.packed

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlin.io.path.createDirectories
import kotlin.io.path.writeText

typealias PackedEntries<T> = Map<PackedKey<T>, T>

typealias MutablePackedEntries<T> = MutableMap<PackedKey<T>, T>

abstract class PackedComponent<T : Any>(val entries: PackedEntries<T>) : PackedExportable {
    companion object

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
