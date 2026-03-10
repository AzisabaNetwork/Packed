package net.azisaba.packed.items.properties

import net.azisaba.packed.KeySerializer
import net.azisaba.packed.RGBLikeSerializer
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable
import net.kyori.adventure.key.Key
import net.kyori.adventure.util.RGBLike

@Serializable
sealed interface PackTintSource {
    @Serializable(with = KeySerializer::class)
    val type: Key
}

@Serializable
data class PackConstantTintSource(
    @Serializable(with = RGBLikeSerializer::class)
    val value: RGBLike,
) : net.azisaba.packed.items.properties.PackTintSource {
    @EncodeDefault
    override val type: Key = Key.key("constant")
}

@Serializable
data class PackDyeTintSource(
    @Serializable(with = RGBLikeSerializer::class)
    val default: RGBLike,
) : net.azisaba.packed.items.properties.PackTintSource {
    @EncodeDefault
    override val type: Key = Key.key("dye")
}

@Serializable
data class PackFireworkTintSource(
    @Serializable(with = RGBLikeSerializer::class)
    val default: RGBLike,
) : net.azisaba.packed.items.properties.PackTintSource {
    @EncodeDefault
    override val type: Key = Key.key("firework")
}

@Serializable
data class PackGrassTintSource(
    val temperature: Float,
    val downfall: Float,
) : net.azisaba.packed.items.properties.PackTintSource {
    @EncodeDefault
    override val type: Key = Key.key("grass")
}

@Serializable
data class PackMapColor(
    @Serializable(with = RGBLikeSerializer::class)
    val default: RGBLike,
) : net.azisaba.packed.items.properties.PackTintSource {
    @EncodeDefault
    override val type: Key = Key.key("map_color")
}

@Serializable
data class PackPotionTintSource(
    @Serializable(with = RGBLikeSerializer::class)
    val default: RGBLike,
) : net.azisaba.packed.items.properties.PackTintSource {
    @EncodeDefault
    override val type: Key = Key.key("potion")
}

@Serializable
data class PackTeamTintSource(
    @Serializable(with = RGBLikeSerializer::class)
    val default: RGBLike,
) : net.azisaba.packed.items.properties.PackTintSource {
    @EncodeDefault
    override val type: Key = Key.key("team")
}

@Serializable
data class PackCustomModelDataTintSource(
    val default: Int,
    val index: Int? = null,
) : net.azisaba.packed.items.properties.PackTintSource {
    @EncodeDefault
    override val type: Key = Key.key("custom_model_data")
}
