package com.tksimeji.packed.font.provider

import com.tksimeji.packed.KeySerializer
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable
import net.kyori.adventure.key.Key

@Serializable
data class PackUnihexFontProvider(
    @Serializable(with = KeySerializer::class)
    val hexFile: Key,
    val sizeOverrides: List<SizeOverride> = emptyList(),
    override val filter: PackFontFilter? = null,
) : PackFontProvider {
    @EncodeDefault
    override val type: String = "unihex"

    @Serializable
    data class SizeOverride(
        val from: String,
        val to: String,
        val left: Int,
        val right: Int,
    )
}
