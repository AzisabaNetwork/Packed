package net.azisaba.packed

import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.nio.file.FileSystemNotFoundException
import java.net.URI
import kotlin.collections.emptyMap
import kotlin.io.path.createDirectories
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile
import kotlin.reflect.KClass

fun interface PackedSource : PackedExportable {
    companion object Builtins {
        fun directory(path: Path, sourceSubPath: String, targetSubPath: String): PackedSource =
            PackedSource { context ->
                if (!path.isDirectory()) return@PackedSource
                val sourceRoot = path.resolve(sourceSubPath)
                val targetRoot = context.pathResolver.rootPath.resolve(targetSubPath)
                copyTree(sourceRoot, targetRoot)
            }

        fun zip(path: Path, sourceSubPath: String, targetSubPath: String): PackedSource =
            PackedSource { context ->
                if (!path.isRegularFile()) return@PackedSource
                FileSystems.newFileSystem(path).use { fs ->
                    val sourceRoot = fs.getPath("/").resolve(sourceSubPath)
                    val targetRoot = context.pathResolver.rootPath.resolve(targetSubPath)
                    copyTree(sourceRoot, targetRoot)
                }
            }

        fun javaResources(kClass: KClass<*>, sourceSubPath: String, targetSubPath: String): PackedSource =
            PackedSource { context ->
                fun copy(root: Path) {
                    val sourceRoot = root.resolve(sourceSubPath)
                    val targetRoot = context.pathResolver.rootPath.resolve(targetSubPath)
                    copyTree(sourceRoot, targetRoot)
                }

                val sourceUri: URI = kClass.java.protectionDomain.codeSource.location.toURI()
                when (sourceUri.scheme) {
                    "jar" -> withFileSystem(sourceUri) { fileSystem ->
                        copy(fileSystem.getPath("/"))
                    }

                    else -> {
                        val sourcePath = Path.of(sourceUri)
                        if (sourcePath.isDirectory()) {
                            copy(sourcePath)
                        } else {
                            FileSystems.newFileSystem(sourcePath).use { fs ->
                                copy(fs.getPath("/"))
                            }
                        }
                    }
                }
            }

        private fun withFileSystem(uri: URI, action: (java.nio.file.FileSystem) -> Unit) {
            try {
                action(FileSystems.getFileSystem(uri))
            } catch (_: FileSystemNotFoundException) {
                FileSystems.newFileSystem(uri, emptyMap<String, Any>()).use(action)
            }
        }

        private fun copyTree(sourceRoot: Path, targetRoot: Path) {
            if (!sourceRoot.isDirectory()) return

            Files.walk(sourceRoot).use { stream ->
                stream.forEach { resourcePath ->
                    if (resourcePath == sourceRoot) return@forEach

                    val outputPath = targetRoot.resolve(sourceRoot.relativize(resourcePath).toString())

                    if (resourcePath.isDirectory()) {
                        outputPath.createDirectories()
                    } else {
                        outputPath.parent?.createDirectories()
                        Files.copy(resourcePath, outputPath, StandardCopyOption.REPLACE_EXISTING)
                    }
                }
            }
        }
    }
}
