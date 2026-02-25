package net.azisaba.packed.util.ktor

import io.papermc.paper.event.connection.configuration.AsyncPlayerConnectionConfigureEvent
import net.kyori.adventure.resource.ResourcePackRequest
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.plugin.Plugin

internal class PluginDisableListener(private val plugin: Plugin, private val stop: () -> Unit) : Listener {
    @EventHandler
    fun onPluginDisable(event: PluginDisableEvent) {
        if (event.plugin == plugin) stop()
    }
}

internal class AsyncPlayerConnectionConfigureListener(
    private val sender: ResourcePackRequestSender,
    private val request: ResourcePackRequest
) : Listener {
    @EventHandler
    fun onAsyncPlayerConnectionConfigure(event: AsyncPlayerConnectionConfigureEvent) {
        sender.sendRequest(event.connection, request)
    }
}
