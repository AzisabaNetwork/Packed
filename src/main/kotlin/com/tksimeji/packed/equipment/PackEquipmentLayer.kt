package com.tksimeji.packed.equipment

import com.tksimeji.packed.KeySerializer
import com.tksimeji.packed.RGBLikeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.kyori.adventure.key.Key
import net.kyori.adventure.util.RGBLike

@Serializable
data class PackEquipmentLayer(
    @Serializable(with = KeySerializer::class)
    val texture: Key,
    val dyeable: Dyeable? = null,
    val usePlayerTexture: Boolean? = null,
) {
    @Serializable
    data class Dyeable(
        @Serializable(with = RGBLikeSerializer::class)
        val colorWhenUndyed: RGBLike,
    )
}

@Serializable
enum class PackEquipmentLayerKey {
    @SerialName("humanoid")
    HUMANOID,

    @SerialName("humanoid_leggings")
    HUMANOID_LEGGINGS,

    @SerialName("wings")
    WINGS,

    @SerialName("wolf_body")
    WOLF_BODY,

    @SerialName("horse_body")
    HORSE_BODY,

    @SerialName("llama_body")
    LLAMA_BODY,

    @SerialName("happy_ghast_body")
    HAPPY_GHAST_BODY,

    @SerialName("pig_saddle")
    PIG_SADDLE,

    @SerialName("strider_saddle")
    STRIDER_SADDLE,

    @SerialName("camel_saddle")
    CAMEL_SADDLE,

    @SerialName("horse_saddle")
    HORSE_SADDLE,

    @SerialName("donkey_saddle")
    DONKEY_SADDLE,

    @SerialName("mule_saddle")
    MULE_SADDLE,

    @SerialName("SKELETON_HORSE_SADDLE")
    SKELETON_HORSE_SADDLE,

    @SerialName("zombie_horse_saddle")
    ZOMBIE_HORSE_SADDLE,
}
