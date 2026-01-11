package com.tksimeji.packed.font.provider

import kotlinx.serialization.Serializable

@Serializable
data class PackFontFilter(
    val uniform: Boolean? = null,
    val jp: Boolean? = null,
)