package net.azisaba.packed

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Namespaced
import java.nio.file.Path

interface PackedPathResolver {
    val rootPath: Path

    val assetsPath: Path

    fun filePath(key: Key, prefix: String = "", extension: String): Path

    fun dirPath(key: Key, prefix: String = ""): Path

    fun namespacePath(namespace: String): Path

    fun namespacePath(namespace: Namespaced): Path = namespacePath(namespace.namespace())

    companion object {
        fun of(root: Path): PackedPathResolver = SimplePathResolver(root)
    }
}

private class SimplePathResolver(override val rootPath: Path) : PackedPathResolver {
    override val assetsPath: Path = rootPath.resolve("assets")

    override fun filePath(key: Key, prefix: String, extension: String): Path =
        resolveNamespacedPath(key, "$prefix/${key.value()}$extension")

    override fun dirPath(key: Key, prefix: String): Path =
        resolveNamespacedPath(key, "$prefix/${key.value()}")

    override fun namespacePath(namespace: String): Path = assetsPath.resolve(namespace)

    private fun resolveNamespacedPath(namespaced: Namespaced, relativePath: String): Path {
        val namespacePath = namespacePath(namespaced)
        return namespacePath.resolve(relativePath)
    }
}
