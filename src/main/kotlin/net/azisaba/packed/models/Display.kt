package net.azisaba.packed.models

import net.azisaba.packed.Vector3icSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.joml.Vector3ic

@Serializable
data class PackDisplay(
    @Serializable(with = Vector3icSerializer::class)
    val rotation: Vector3ic,
    @Serializable(with = Vector3icSerializer::class)
    val translation: Vector3ic,
    @Serializable(with = Vector3icSerializer::class)
    val scale: Vector3ic,
)

@Serializable
enum class PackDisplayKey {
    @SerialName("thirdperson_righthand")
    THIRDPERSON_RIGHTHAND,

    @SerialName("thirdperson_lefthand")
    THIRDPERSON_LEFTHAND,

    @SerialName("firstperson_righthand")
    FIRSTPERSON_RIGHTHAND,

    @SerialName("firstperson_lefthand")
    FIRSTPERSON_LEFTHAND,

    @SerialName("gui")
    GUI,

    @SerialName("head")
    HEAD,

    @SerialName("ground")
    GROUND,

    @SerialName("fixed")
    FIXED,

    @SerialName("on_shelf")
    ON_SHELF,
}
