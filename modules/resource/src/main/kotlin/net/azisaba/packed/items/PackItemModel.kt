package net.azisaba.packed.items

import kotlinx.serialization.Serializable
import net.azisaba.packed.items.properties.PackItemModelProperties

@Serializable
data class PackItemModel(
    val model: PackItemModelProperties,
    val handAnimationOnSwap: Boolean? = null,
    val oversizedInGui: Boolean? = null,
    val swapAnimationScale: Float? = null,
)
