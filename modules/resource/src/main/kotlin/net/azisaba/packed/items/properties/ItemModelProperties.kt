package net.azisaba.packed.items.properties

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable
import net.azisaba.packed.PackedKey
import net.azisaba.packed.PackedKeySerializer
import net.azisaba.packed.items.PackItemModel
import net.azisaba.packed.models.PackModel
import net.azisaba.serialization.KeySerializer
import net.kyori.adventure.key.Key

@Serializable
sealed interface PackItemModelProperties {
    val type: Key
}

@Serializable
data class PackModelItemModelProperties(
    @Serializable(with = PackedKeySerializer::class)
    val model: PackedKey<PackModel>,
    val tints: List<PackTintSource>? = null,
) : PackItemModelProperties {
    @EncodeDefault
    @Serializable(with = KeySerializer::class)
    override val type: Key = Key.key("model")
}

@Serializable
data class PackSpecialItemModelProperties(
    @Serializable(with = KeySerializer::class)
    val base: Key,
    val model: PackSpecialModel,
) : PackItemModelProperties {
    @EncodeDefault
    @Serializable(with = KeySerializer::class)
    override val type: Key = Key.key("special")
}

@Serializable
data class PackCompositeItemModelProperties(
    val models: List<PackItemModel>,
) : PackItemModelProperties {
    @EncodeDefault
    @Serializable(with = KeySerializer::class)
    override val type: Key = Key.key("composite")
}

@Serializable
class PackEmptyItemModelProperties : PackItemModelProperties {
    @EncodeDefault
    @Serializable(with = KeySerializer::class)
    override val type: Key = Key.key("empty")
}

@Serializable
class PackBundleSelectedItemItemModelProperties : PackItemModelProperties {
    @EncodeDefault
    @Serializable(with = KeySerializer::class)
    override val type: Key = Key.key("bundle/selected_item")
}
