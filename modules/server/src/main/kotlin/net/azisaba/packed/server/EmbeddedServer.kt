package net.azisaba.packed.server

import io.ktor.server.application.Application
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import net.azisaba.packed.Packed
import java.nio.file.Files
import java.security.MessageDigest

data class ConfiguredEmbeddedServer<TEngine : ApplicationEngine, TConfiguration : ApplicationEngine.Configuration>(
    val embeddedServer: EmbeddedServer<TEngine, TConfiguration>,
    val tempDirectoryPath: java.nio.file.Path,
    val zipPath: java.nio.file.Path,
) {
    fun computeSha1Hash(): String {
        val messageDigest = MessageDigest.getInstance("SHA-1")
        Files.newInputStream(zipPath).use { input ->
            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
            while (true) {
                val read = input.read(buffer)
                if (read == -1) break
                messageDigest.update(buffer, 0, read)
            }
        }
        return messageDigest.digest().joinToString("") { "%02x".format(it) }
    }
}

fun Packed.configureEmbeddedServer(
    port: Int = 8080,
    host: String = "0.0.0.0",
    watchPaths: List<String> = listOf(SystemFileSystem.resolve(Path(".")).toString()),
    configureAction: Application.() -> Unit,
): ConfiguredEmbeddedServer<NettyApplicationEngine, NettyApplicationEngine.Configuration> {
    val tempDirectoryPath = Files.createTempDirectory("packed-server-")
    val zipPath = tempDirectoryPath.resolve("pack.zip")

    exportZip(zipPath)

    val server = embeddedServer(Netty, port, host, watchPaths) {
        configureRouting(zipPath)
        configureMonitor(tempDirectoryPath)
        configureAction()
    }

    return ConfiguredEmbeddedServer(server, tempDirectoryPath, zipPath)
}
