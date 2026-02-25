package net.azisaba.packed

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.ClassDiscriminatorMode
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import net.azisaba.packed.util.PackPathResolver
import net.azisaba.packed.util.SimplePathResolver
import java.nio.file.Files
import java.nio.file.Path
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.io.path.createDirectories

@OptIn(ExperimentalSerializationApi::class)
private val defaultJson: Json = Json {
    prettyPrint = true
    namingStrategy = JsonNamingStrategy.SnakeCase
    classDiscriminatorMode = ClassDiscriminatorMode.NONE
}

fun Pack.build(path: Path, json: Json = defaultJson) =
    buildWithPathResolver(SimplePathResolver(path), json)

fun Pack.buildZip(zipPath: Path, json: Json = defaultJson) {
    val tempRoot = Files.createTempDirectory("packed-build-")
    try {
        buildWithPathResolver(SimplePathResolver(tempRoot), json)
        zipPath.parent?.createDirectories()
        ZipOutputStream(Files.newOutputStream(zipPath)).use { zip ->
            Files.walk(tempRoot).use { stream ->
                stream.filter { !Files.isDirectory(it) }.forEach { file ->
                    val entryName = tempRoot.relativize(file).toString().replace('\\', '/')
                    zip.putNextEntry(ZipEntry(entryName))
                    Files.copy(file, zip)
                    zip.closeEntry()
                }
            }
        }
    } finally {
        tempRoot.toFile().deleteRecursively()
    }
}

private fun Pack.buildWithPathResolver(pathResolver: PackPathResolver, json: Json) {
    for ((type, set) in resourceMap) {
        @Suppress("UNCHECKED_CAST")
        (type as PackResourceType<Any>).build(json, pathResolver, set as Set<Any>)
    }
}
