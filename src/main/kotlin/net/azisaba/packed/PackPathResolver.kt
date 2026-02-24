package net.azisaba.packed

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Namespaced
import java.nio.file.Path

interface PackPathResolver {
    val assetsPath: Path

    fun filePath(key: Key, prefix: String = "", extension: String): Path

    fun dirPath(key: Key, prefix: String = ""): Path

    fun namespacePath(namespace: Namespaced): Path

    fun namespacePath(namespace: String): Path
}
