package net.azisaba.packed

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.ClassDiscriminatorMode
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import net.azisaba.packed.dsl.PackedBuilder
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.io.path.deleteIfExists
import kotlin.io.path.isDirectory
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

    fun exportZip(zipPath: Path, json: Json = defaultJson) {
        val tempDir = Files.createTempDirectory("packed-")

        try {
            export(tempDir, json)

            ZipOutputStream(
                Files.newOutputStream(
                    zipPath,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
                )
            ).use { zip ->
                Files.walk(tempDir).use { paths ->
                    paths.forEach { path ->
                        val entryName = tempDir.relativize(path)
                            .toString()
                            .replace(File.separatorChar, '/')

                        if (!path.isDirectory()) {
                            zip.putNextEntry(ZipEntry(entryName))
                            Files.copy(path, zip)
                            zip.closeEntry()
                        }
                    }
                }
            }
        } finally {
            Files.walk(tempDir).use { stream ->
                stream.sorted(Comparator.reverseOrder())
                    .forEach(Path::deleteIfExists)
            }
        }
    }

    private fun exportMetadata(context: PackedExportContext) {
        val jsonString = context.json.encodeToString(metadata)
        val path = context.pathResolver.rootPath.resolve("pack.mcmeta")
        path.writeText(jsonString)
    }
}

fun Packed(builderAction: PackedBuilder.() -> Unit): Packed {
    val builder = PackedBuilder()
    builder.builderAction()
    return builder.build()
}
