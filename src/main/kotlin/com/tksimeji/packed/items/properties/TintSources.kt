package com.tksimeji.packed.items.properties

import com.tksimeji.packed.KeySerializer
import com.tksimeji.packed.RGBLikeSerializer
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
) : PackTintSource {
    @EncodeDefault
    override val type: Key = Key.key("constant")
}

@Serializable
data class PackDyeTintSource(
    @Serializable(with = RGBLikeSerializer::class)
    val default: RGBLike,
) : PackTintSource {
    @EncodeDefault
    override val type: Key = Key.key("dye")
}

@Serializable
data class PackFireworkTintSource(
    @Serializable(with = RGBLikeSerializer::class)
    val default: RGBLike,
) : PackTintSource {
    @EncodeDefault
    override val type: Key = Key.key("firework")
}

@Serializable
data class PackGrassTintSource(
    val temperature: Float,
    val downfall: Float,
) : PackTintSource {
    @EncodeDefault
    override val type: Key = Key.key("grass")
}

@Serializable
data class PackMapColor(
    @Serializable(with = RGBLikeSerializer::class)
    val default: RGBLike,
) : PackTintSource {
    @EncodeDefault
    override val type: Key = Key.key("map_color")
}

@Serializable
data class PackPotionTintSource(
    @Serializable(with = RGBLikeSerializer::class)
    val default: RGBLike,
) : PackTintSource {
    @EncodeDefault
    override val type: Key = Key.key("potion")
}

@Serializable
data class PackTeamTintSource(
    @Serializable(with = RGBLikeSerializer::class)
    val default: RGBLike,
) : PackTintSource {
    @EncodeDefault
    override val type: Key = Key.key("team")
}

@Serializable
data class PackCustomModelDataTintSource(
    val default: Int,
    val index: Int? = null,
) : PackTintSource {
    @EncodeDefault
    override val type: Key = Key.key("custom_model_data")
}
