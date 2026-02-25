package net.azisaba.packed.sounds

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.azisaba.packed.util.KeySerializer
import net.kyori.adventure.key.Key

@Serializable
data class PackSound(
    val type: PackSoundType,
    @Serializable(with = KeySerializer::class)
    val name: Key,
    val volume: Float = 1f,
    val pitch: Float = 1f,
    val weight: Int = 1,
    val attenuationDistance: Int = 16,
    val preload: Boolean = false,
    val stream: Boolean = false,
)

@Serializable
enum class PackSoundType {
    @SerialName("file")
    FILE,

    @SerialName("event")
    EVENT
}
