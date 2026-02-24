package net.azisaba.packed.models

import net.azisaba.packed.KeySerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.kyori.adventure.key.Key

@Serializable
data class PackModel(
    val parent: Key? = null,
    val textures: Map<String, @Serializable(with = KeySerializer::class) Key> = emptyMap(),
    val display: Map<PackDisplayKey, PackDisplay> = emptyMap(),
    val elements: List<PackElement> = emptyList(),
    val guiLight: PackGuiLight? = null,
    val ambientocclusion: Boolean? = null,
)

@Serializable
enum class PackGuiLight {
    @SerialName("side")
    SIDE,

    @SerialName("front")
    FRONT,
}
