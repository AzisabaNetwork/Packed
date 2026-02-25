package net.azisaba.packed.util

import kotlinx.serialization.Serializable

@Serializable
data class PackFormat(val major: Int, val minor: Int = 0)

@Serializable
data class PackFormatRange(val minInclusive: Int, val maxInclusive: Int)
