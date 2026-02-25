package net.azisaba.packed

import kotlinx.serialization.Serializable
import net.azisaba.packed.util.ComponentSerializer
import net.kyori.adventure.text.Component

@Serializable
data class PackMetadata(val pack: Info) {
    @Serializable
    data class Info(
        val packFormat: PackFormat?,
        val minFormat: PackFormat?,
        val maxFormat: PackFormat?,
        val supportedFormats: Set<PackFormatRange> = emptySet(),
        @Serializable(with = ComponentSerializer::class)
        val description: Component? = null,
    )
}
