package net.azisaba.packed

import kotlinx.serialization.Serializable
import net.azisaba.packed.util.ComponentSerializer
import net.kyori.adventure.text.Component

@Serializable
data class PackMetadata(val pack: Info) {
    @Serializable
    data class Info(
        val minFormat: PackFormat,
        val maxFormat: PackFormat,
        val packFormat: Int?,
        @Serializable(with = ComponentSerializer::class)
        val description: Component,
    )
}
