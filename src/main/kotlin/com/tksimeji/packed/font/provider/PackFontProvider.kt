package com.tksimeji.packed.font.provider

import kotlinx.serialization.Serializable

@Serializable
sealed interface PackFontProvider {
    val type: String

    val filter: PackFontFilter?
}