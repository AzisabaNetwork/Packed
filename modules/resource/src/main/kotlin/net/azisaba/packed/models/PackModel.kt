package net.azisaba.packed.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.azisaba.packed.KeySerializer
import net.azisaba.packed.PackedKey
import net.azisaba.packed.PackedKeySerializer
import net.azisaba.packed.model
import net.kyori.adventure.key.Key

@Serializable
data class PackModel(
    @Serializable(with = PackedKeySerializer::class)
    val parent: PackedKey<PackModel>? = null,
    val textures: Map<String, @Serializable(with = KeySerializer::class) Key> = emptyMap(),
    val display: Map<PackDisplayKey, PackDisplay> = emptyMap(),
    val elements: List<PackElement> = emptyList(),
    val guiLight: PackGuiLight? = null,
    val ambientocclusion: Boolean? = null,
) {
    companion object {
        val BLOCK_BLOCK: PackedKey<PackModel> = PackedKey.model(Key.key("block/block"))
        val BUILTIN_GENERATED: PackedKey<PackModel> = PackedKey.model(Key.key("builtin/generated"))
        val BUILTIN_MISSING: PackedKey<PackModel> = PackedKey.model(Key.key("builtin/missing"))
        val ITEM_GENERATED: PackedKey<PackModel> = PackedKey.model(Key.key("item/generated"))

        fun item(vararg layers: Key): PackModel = PackModel(
            parent = ITEM_GENERATED,
            textures = layers.mapIndexed { index, value -> "layer$index" to value }.toMap(),
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
