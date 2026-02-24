package net.azisaba.packed.items

import net.azisaba.packed.items.properties.PackItemModelProperties
import kotlinx.serialization.Serializable

@Serializable
data class PackItemModel(
    val model: PackItemModelProperties,
    val handAnimationOnSwap: Boolean? = null,
    val oversizedInGui: Boolean? = null,
    val swapAnimationScale: Float? = null,
)
