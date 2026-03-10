package net.azisaba.packed.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.azisaba.packed.KeySerializer
import net.kyori.adventure.key.Key

@Serializable
data class PackModel(
    @Serializable(with = KeySerializer::class)
    val parent: Key? = null,
    val textures: Map<String, @Serializable(with = KeySerializer::class) Key> = emptyMap(),
    val display: Map<net.azisaba.packed.models.PackDisplayKey, net.azisaba.packed.models.PackDisplay> = emptyMap(),
    val elements: List<PackElement> = emptyList(),
    val guiLight: net.azisaba.packed.models.PackGuiLight? = null,
    val ambientocclusion: Boolean? = null,
)

@Serializable
enum class PackGuiLight {
    @SerialName("side")
    SIDE,

    @SerialName("front")
    FRONT,
}
