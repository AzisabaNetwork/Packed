package net.azisaba.packed.util.ktor

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.papermc.paper.connection.PlayerConfigurationConnection
import io.papermc.paper.event.connection.configuration.AsyncPlayerConnectionConfigureEvent
import net.azisaba.packed.Pack
import net.azisaba.packed.buildZip
import net.kyori.adventure.resource.ResourcePackInfo
import net.kyori.adventure.resource.ResourcePackRequest
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.plugin.Plugin
import java.nio.file.Files
import java.nio.file.Path
import java.nio.charset.StandardCharsets
import java.net.URI
import java.security.MessageDigest
import java.util.concurrent.atomic.AtomicBoolean
import java.util.UUID
import kotlin.io.path.createDirectories
import kotlin.io.path.deleteIfExists

fun Pack.launchKtor(
    plugin: Plugin,
    port: Int = 8080,
    host: String = "0.0.0.0",
    resourcePackRequestSender: ResourcePackRequestSender? = null,
    configure: Application.() -> Unit = {},
): EmbeddedServer<NettyApplicationEngine, NettyApplicationEngine.Configuration> {
    require(port in 1..65535) { "port must be in 1..65535" }

    val tempDirectory = Files.createTempDirectory("packed-server-")
    val packPath = tempDirectory.resolve("pack.zip")

    tempDirectory.createDirectories()
    buildZip(packPath)

    val server = embeddedServer(Netty, host = host, port = port) {
        configureRouting(packPath)
        configure()
    }
    server.start(wait = false)

    val stopped = AtomicBoolean(false)
    val stopServer: () -> Unit = {
        if (stopped.compareAndSet(false, true)) {
            server.stop(1_000, 5_000)
            packPath.deleteIfExists()
            tempDirectory.toFile().deleteRecursively()
        }
    }

    plugin.server.pluginManager.registerEvents(PluginDisableListener(plugin, stopServer), plugin)
    resourcePackRequestSender?.let { sender ->
        val request = sender.createRequest(host, port, packPath)
        plugin.server.pluginManager.registerEvents(AsyncPlayerConnectionConfigureListener(sender, request), plugin)
    }

    Runtime.getRuntime().addShutdownHook(
        Thread {
            stopServer()
        },
    )

    return server
}

private fun Application.configureRouting(packPath: Path) {
    routing {
        get("/") {
            call.respondFile(packPath.toFile())
        }
    }
}

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

private class PluginDisableListener(private val plugin: Plugin, private val stop: () -> Unit) : Listener {
    @EventHandler
    fun onPluginDisable(event: PluginDisableEvent) {
        if (event.plugin == plugin) stop()
    }
}

private class AsyncPlayerConnectionConfigureListener(
    private val sender: ResourcePackRequestSender,
    private val request: ResourcePackRequest
) : Listener {
    @EventHandler
    fun onAsyncPlayerConnectionConfigure(event: AsyncPlayerConnectionConfigureEvent) {
        sender.sendRequest(event.connection, request)
    }
}
