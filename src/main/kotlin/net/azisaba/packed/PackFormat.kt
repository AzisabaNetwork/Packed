package net.azisaba.packed

import kotlinx.serialization.Serializable
import net.azisaba.packed.util.PackFormatSerializer

@Serializable(with = PackFormatSerializer::class)
data class PackFormat(val major: Int, val minor: Int = 0)
