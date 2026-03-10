package net.azisaba.packed.ktor

import io.ktor.server.engine.*
import net.kyori.adventure.resource.ResourcePackInfo
import org.jetbrains.annotations.ApiStatus
import java.net.URI
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.security.MessageDigest
import java.util.*
import kotlin.io.path.deleteIfExists

@ApiStatus.Experimental
data class PackedServer<TEngine : ApplicationEngine, TConfiguration : ApplicationEngine.Configuration>(
    val server: EmbeddedServer<TEngine, TConfiguration>,
    val tempDirectory: Path,
    val zipPath: Path,
) {
    fun stop(gracePeriodMillis: Long = 1000, timeoutMillis: Long = 5000) {
        server.stop(gracePeriodMillis, timeoutMillis)
        Files.walk(tempDirectory).use { stream ->
            stream.sorted(Comparator.reverseOrder())
                .forEach(Path::deleteIfExists)
        }
    }

    fun createResourcePackInfo(uri: URI): ResourcePackInfo {
        val sha1 = computeSha1()
        val packId = UUID.nameUUIDFromBytes("$uri#$sha1".toByteArray(StandardCharsets.UTF_8))
        return ResourcePackInfo.resourcePackInfo(packId, uri, sha1)
    }

    fun computeSha1(): String {
        val md = MessageDigest.getInstance("SHA-1")
        Files.newInputStream(zipPath).use { input ->
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
