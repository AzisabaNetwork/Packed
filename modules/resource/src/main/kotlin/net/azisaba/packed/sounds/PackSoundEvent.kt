package net.azisaba.packed.sounds

import kotlinx.serialization.Serializable
import net.azisaba.packed.KeySerializer
import net.kyori.adventure.key.Key

@Serializable
data class PackSoundEvent(
    val sounds: Collection<PackSound>,
    @Serializable(with = KeySerializer::class)
    val subtitle: Key? = null,
    val replace: Boolean = false,
)
