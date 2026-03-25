package net.azisaba.packed

import kotlinx.serialization.Serializable
import net.azisaba.serialization.ComponentJsonSerializer
import net.kyori.adventure.text.Component

@Serializable
data class PackMeta(val pack: Info) {
    @Serializable
    data class Info(
        val minFormat: PackFormat,
        val maxFormat: PackFormat,
        val packFormat: Int?,
        @Serializable(with = ComponentJsonSerializer::class)
        val description: Component,
    )
}

@Serializable(with = PackFormatSerializer::class)
data class PackFormat(val major: Int, val minor: Int = 0)
