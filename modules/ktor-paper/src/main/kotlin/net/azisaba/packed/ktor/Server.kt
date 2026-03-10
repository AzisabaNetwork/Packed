package net.azisaba.packed.ktor

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import net.azisaba.packed.Packed
import net.kyori.adventure.resource.ResourcePackRequest
import org.bukkit.plugin.Plugin
import org.jetbrains.annotations.ApiStatus
import java.net.URI
import java.nio.file.Files

@ApiStatus.Experimental
fun Packed.createKtorServer(
    port: Int = 8080,
    host: String = "0.0.0.0",
    configureAction: Application.() -> Unit = {},
): PackedServer<NettyApplicationEngine, NettyApplicationEngine.Configuration> {
    val tempDirectory = Files.createTempDirectory("packed-server-")
    val zipPath = tempDirectory.resolve("pack.zip")

    exportZip(zipPath)

    val server = embeddedServer(Netty, port, host) {
        configureRouting(zipPath)
        configureAction()
    }

    return PackedServer(server, tempDirectory, zipPath)
}

@ApiStatus.Experimental
fun Packed.startKtorServer(
    plugin: Plugin,
    port: Int = 8080,
    host: String = "0.0.0.0",
    configureAction: Application.() -> Unit = {},
    resourcePackRequest: (PackedServer<NettyApplicationEngine, NettyApplicationEngine.Configuration>) -> ResourcePackRequest = { server ->
        val host = when (host) {
            "0.0.0.0", "::", "::0", "[::]" -> plugin.server.ip.ifBlank { "127.0.0.1" }
            else -> host
        }
        ResourcePackRequest.resourcePackRequest()
            .packs(server.createResourcePackInfo(URI.create("http://$host:$port/")))
            .required(true)
            .replace(false)
            .build()
    },
): PackedServer<NettyApplicationEngine, NettyApplicationEngine.Configuration> {
    val server = createKtorServer(port, host, configureAction)

    server.server.start(wait = false)

    plugin.server.pluginManager.registerEvents(PluginDisableListener(plugin, server::stop), plugin)
    plugin.server.pluginManager.registerEvents(AsyncPlayerConnectionConfigureListener(resourcePackRequest(server)), plugin)

    return server
}
