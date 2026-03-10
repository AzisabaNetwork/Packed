package net.azisaba.packed

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Namespaced
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.writeText

fun interface ResourceWriter<T : Any> {
    fun write(context: ResourceWriteContext, type: ResourceType<T>)

    companion object {
        fun <T : Any> jsonFilePerResource(pathPrefix: String, kSerializer: KSerializer<T>): ResourceWriter<T> =
            ResourceWriter { context, type ->
                for ((resourceKey, resource) in context.resourceAccess.collectResources(type)) {
                    val path = context.pathResolver.filePath(resourceKey, pathPrefix, extension = ".json")
                    if (!path.exists()) path.parent.createDirectories()

                    val jsonString = context.json.encodeToString(kSerializer, resource)
                    path.writeText(jsonString)
                }
            }

        fun <T : Any> jsonFilePerNamespace(fileLocation: String, kSerializer: KSerializer<T>): ResourceWriter<T> =
            ResourceWriter { context, type ->
                val resources = context.resourceAccess.collectResources(type)

                val grouped = resources.entries.associate { it.key.namespace() to (it.key to it.value) }

                for ((namespace, entries) in grouped) {
                    val map = entries.associate { it.key.key().value() to it.value }

                    val path = context.pathResolver.filePath(Key.key(namespace, fileLocation), extension = ".json")
                    if (!path.exists()) path.parent.createDirectories()

                    val jsonString = context.json.encodeToString(
                        MapSerializer(String.serializer(), kSerializer),
                        map
                    )

                    path.writeText(jsonString)
                }
            }
    }
}

data class ResourceWriteContext(
    val resourceAccess: ResourceAccess,
    val pathResolver: ResourcePathResolver,
    val json: Json,
)

interface ResourcePathResolver {
    val rootPath: Path

    val assetsPath: Path

    fun filePath(key: Key, prefix: String = "", extension: String): Path

    fun dirPath(key: Key, prefix: String = ""): Path

    fun namespacePath(namespace: String): Path

    fun namespacePath(namespace: Namespaced): Path = namespacePath(namespace.namespace())

    companion object {
        fun wrap(root: Path): ResourcePathResolver = ResourcePathResolverImpl(root)
    }
}

private class ResourcePathResolverImpl(override val rootPath: Path) : ResourcePathResolver {
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
