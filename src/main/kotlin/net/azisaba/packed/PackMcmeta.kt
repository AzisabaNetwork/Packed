package net.azisaba.packed

import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component

@Serializable
data class PackMcmeta(
    val pack: Pack,
) {
    @Serializable
    data class Pack(
        val minFormat: PackFormat,
        val maxFormat: PackFormat,
        @Serializable(with = ComponentSerializer::class)
        val description: Component,
    )
}

@Serializable
data class PackFormat(val major: Int, val minor: Int = 0)
