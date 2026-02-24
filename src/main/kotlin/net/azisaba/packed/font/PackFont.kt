package net.azisaba.packed.font

import net.azisaba.packed.font.provider.PackFontProvider
import kotlinx.serialization.Serializable

@Serializable
data class PackFont(
    val providers: List<PackFontProvider>
)
