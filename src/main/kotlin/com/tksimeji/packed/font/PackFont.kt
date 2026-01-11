package com.tksimeji.packed.font

import com.tksimeji.packed.font.provider.PackFontProvider
import kotlinx.serialization.Serializable

@Serializable
data class PackFont(
    val providers: List<PackFontProvider>
)
