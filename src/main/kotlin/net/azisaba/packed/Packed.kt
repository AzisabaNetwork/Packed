package net.azisaba.packed

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.ClassDiscriminatorMode
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import java.nio.file.Path
import kotlin.io.path.writeText

@OptIn(ExperimentalSerializationApi::class)
private val defaultJson: Json = Json {
    prettyPrint = true
    namingStrategy = JsonNamingStrategy.SnakeCase
    classDiscriminatorMode = ClassDiscriminatorMode.NONE
    explicitNulls = false
}

class Packed(
    val metadata: PackMeta,
    val components: Collection<PackedComponent<*>>,
    val otherSources: Collection<PackedSource>,
) : PackedExportable {
    fun export(path: Path, json: Json = defaultJson) = export(PackedExportContext(PackedPathResolver.of(path), json))

    override fun export(context: PackedExportContext) {
        exportMetadata(context)
        for (component in components) {
            component.export(context)
        }
        for (source in otherSources) {
            source.export(context)
        }
    }

    private fun exportMetadata(context: PackedExportContext) {
        val jsonString = context.json.encodeToString(metadata)
        val path = context.pathResolver.rootPath.resolve("pack.mcmeta")
        path.writeText(jsonString)
    }
}
