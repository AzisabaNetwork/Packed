package net.azisaba.packed.util

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Namespaced
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile
import kotlin.io.path.exists

interface PackPathResolver {
    val assetsPath: Path

    fun filePath(key: Key, prefix: String = "", extension: String): Path

    fun dirPath(key: Key, prefix: String = ""): Path

    fun namespacePath(namespace: Namespaced): Path

    fun namespacePath(namespace: String): Path
}

internal class SimplePathResolver(rootPath: Path) : PackPathResolver {
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
