package net.azisaba.packed

import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import kotlin.io.path.createDirectories
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile
import kotlin.reflect.KClass

fun interface PackedSource : PackedExportable {
    companion object Builtins {
        fun directory(path: Path, sourceSubPath: Path = Path.of(""), targetSubPath: Path = Path.of("")): PackedSource =
            PackedSource { context ->
                if (!path.isDirectory()) return@PackedSource
                val sourceRoot = path.resolve(sourceSubPath)
                val targetRoot = context.pathResolver.rootPath.resolve(targetSubPath)
                copyTree(sourceRoot, targetRoot)
            }

        fun zip(path: Path, sourceSubPath: Path = Path.of(""), targetSubPath: Path = Path.of("")): PackedSource =
            PackedSource { context ->
                if (!path.isRegularFile()) return@PackedSource
                FileSystems.newFileSystem(path).use { fs ->
                    val sourceRoot = fs.getPath("/").resolve(sourceSubPath)
                    val targetRoot = context.pathResolver.rootPath.resolve(targetSubPath)
                    copyTree(sourceRoot, targetRoot)
                }
            }

        fun javaResources(
            kClass: KClass<*>,
            sourceSubPath: Path = Path.of("assets"),
            targetSubPath: Path = Path.of("assets")
        ): PackedSource = PackedSource { context ->
            val sourcePath = Path.of(kClass.java.protectionDomain.codeSource.location.toURI())

            fun copy(root: Path) {
                val sourceRoot = root.resolve(sourceSubPath)
                val targetRoot = context.pathResolver.rootPath.resolve(targetSubPath)
                copyTree(sourceRoot, targetRoot)
            }

            if (sourcePath.isDirectory()) {
                copy(sourcePath)
            } else {
                FileSystems.newFileSystem(sourcePath).use { fs ->
                    copy(fs.getPath("/"))
                }
            }
        }

        private fun copyTree(sourceRoot: Path, targetRoot: Path) {
            if (!sourceRoot.isDirectory()) return

            Files.walk(sourceRoot).use { stream ->
                stream.forEach { resourcePath ->
                    if (resourcePath == sourceRoot) return@forEach

                    val outputPath = targetRoot.resolve(sourceRoot.relativize(resourcePath))

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
