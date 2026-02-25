package net.azisaba.packed.items

import net.azisaba.packed.items.properties.PackItemModelProperties
import kotlinx.serialization.Serializable
import net.azisaba.packed.KeyedJsonPerFileResourceType
import net.azisaba.packed.util.KeyedPackResource
import net.azisaba.packed.PackResourceType

@Serializable
data class PackItemModel(
    val model: PackItemModelProperties,
    val handAnimationOnSwap: Boolean? = null,
    val oversizedInGui: Boolean? = null,
    val swapAnimationScale: Float? = null,
) {
    internal companion object {
        fun resourceType(): PackResourceType<KeyedPackResource<PackItemModel>> = KeyedJsonPerFileResourceType(
            "items", serializer()
        )
    }
}
