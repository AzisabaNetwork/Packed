package net.azisaba.packed.util.ktor

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import net.azisaba.packed.Pack
import net.azisaba.packed.util.builder.buildZip
import org.bukkit.plugin.Plugin
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.atomic.AtomicBoolean
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
