package net.azisaba.packed.util.ktor

import io.papermc.paper.connection.PlayerConfigurationConnection
import net.kyori.adventure.resource.ResourcePackInfo
import net.kyori.adventure.resource.ResourcePackRequest
import org.bukkit.Bukkit
import java.net.URI
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.security.MessageDigest
import java.util.UUID

interface ResourcePackRequestSender {
    fun createRequest(bindHost: String, bindPort: Int, packPath: Path): ResourcePackRequest

    fun sendRequest(connection: PlayerConfigurationConnection, request: ResourcePackRequest)

    open class Simple : ResourcePackRequestSender {
        override fun createRequest(bindHost: String, bindPort: Int, packPath: Path): ResourcePackRequest {
            val host = when (bindHost) {
                "0.0.0.0", "::", "::0", "[::]" -> Bukkit.getIp().ifBlank { "127.0.0.1" }
                else -> bindHost
            }
            val uri = URI.create("http://${host}:$bindPort")
            val computedSha1Hex = computeSha1Hex(packPath)

            val packId = UUID.nameUUIDFromBytes("$uri#$computedSha1Hex".toByteArray(StandardCharsets.UTF_8))
            val packInfo = ResourcePackInfo.resourcePackInfo(packId, uri, computedSha1Hex)

            return ResourcePackRequest.resourcePackRequest()
                .packs(packInfo)
                .replace(true)
                .required(true)
                .build()
        }

        override fun sendRequest(connection: PlayerConfigurationConnection, request: ResourcePackRequest) {
            connection.audience.sendResourcePacks(request)
        }

        protected fun computeSha1Hex(file: Path): String {
            val md = MessageDigest.getInstance("SHA-1")
            Files.newInputStream(file).use { input ->
                val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                while (true) {
                    val read = input.read(buffer)
                    if (read == -1) break
                    md.update(buffer, 0, read)
                }
            }
            return md.digest().joinToString("") { "%02x".format(it) }
        }
    }
}
