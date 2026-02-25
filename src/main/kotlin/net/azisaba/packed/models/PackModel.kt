package net.azisaba.packed.models

import net.azisaba.packed.util.KeySerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.azisaba.packed.KeyedJsonPerFileResourceType
import net.azisaba.packed.util.KeyedPackResource
import net.azisaba.packed.PackResourceType
import net.kyori.adventure.key.Key

@Serializable
data class PackModel(
    val parent: Key? = null,
    val textures: Map<String, @Serializable(with = KeySerializer::class) Key> = emptyMap(),
    val display: Map<PackDisplayKey, PackDisplay> = emptyMap(),
    val elements: List<PackElement> = emptyList(),
    val guiLight: PackGuiLight? = null,
    val ambientocclusion: Boolean? = null,
) {
    internal companion object {
        fun resourceType(): PackResourceType<KeyedPackResource<PackModel>> = KeyedJsonPerFileResourceType(
            "models", serializer()
        )
    }
}

@Serializable
enum class PackGuiLight {
    @SerialName("side")
    SIDE,

    @SerialName("front")
    FRONT,
}
