package net.azisaba.packed.font

import kotlinx.serialization.Serializable
import net.azisaba.packed.font.provider.PackFontProvider

@Serializable
data class PackFont(
    val providers: List<net.azisaba.packed.font.provider.PackFontProvider>
)
