package net.azisaba.packed

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.ClassDiscriminatorMode
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Namespaced
import java.nio.file.Files
import java.nio.file.Path
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile
import kotlin.io.path.exists

@OptIn(ExperimentalSerializationApi::class)
private val defaultJson: Json = Json {
    prettyPrint = true
    namingStrategy = JsonNamingStrategy.SnakeCase
    classDiscriminatorMode = ClassDiscriminatorMode.NONE
}

fun Packed.build(path: Path, json: Json = defaultJson) =
    buildWithPathResolver(SimplePathResolver(path), json)

fun Packed.buildZip(zipPath: Path, json: Json = defaultJson) {
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

private fun Packed.buildWithPathResolver(pathResolver: PackPathResolver, json: Json) {
    for ((type, set) in resourceMap) {
        @Suppress("UNCHECKED_CAST")
        (type as Packed.Type<Any>).build(json, pathResolver, set as Set<Any>)
    }
}

private class SimplePathResolver(rootPath: Path) : PackPathResolver {
    override val assetsPath: Path by lazy {
        val path = rootPath.resolve("assets")
        if (!path.exists()) {
            path.createDirectories()
        }
        path
    }

    override fun filePath(key: Key, prefix: String, extension: String): Path =
        resolveNamespacedPath(key, "$prefix/${key.value()}$extension").createFileIfNotExists()

    override fun dirPath(key: Key, prefix: String): Path =
        resolveNamespacedPath(key, "$prefix/${key.value()}").createDirectories()

    override fun namespacePath(namespace: Namespaced): Path = namespacePath(namespace.namespace())

    override fun namespacePath(namespace: String): Path {
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
