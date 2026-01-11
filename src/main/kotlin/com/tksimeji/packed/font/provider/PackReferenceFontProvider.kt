package com.tksimeji.packed.font.provider

import com.tksimeji.packed.KeySerializer
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable
import net.kyori.adventure.key.Key

@Serializable
data class PackReferenceFontProvider(
    @Serializable(with = KeySerializer::class)
    val id: Key,
    override val filter: PackFontFilter? = null,
) : PackFontProvider {
    @EncodeDefault
    override val type: String = "reference"
}
