package com.tksimeji.packed.font.provider

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable

@Serializable
data class PackSpaceFontProvider(
    val advances: Map<Char, Int>,
    override val filter: PackFontFilter? = null,
) : PackFontProvider {
    @EncodeDefault
    override val type: String = "space"
}
