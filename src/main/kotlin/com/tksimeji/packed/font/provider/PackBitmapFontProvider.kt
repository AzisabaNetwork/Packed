package com.tksimeji.packed.font.provider

import com.tksimeji.packed.KeySerializer
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable
import net.kyori.adventure.key.Key

@Serializable
data class PackBitmapFontProvider(
    @Serializable(with = KeySerializer::class)
    val file: Key,
    val chars: List<String>,
    val ascent: Int,
    val height: Int,
    override val filter: PackFontFilter? = null,
) : PackFontProvider {
    @EncodeDefault
    override val type: String = "bitmap"
}

