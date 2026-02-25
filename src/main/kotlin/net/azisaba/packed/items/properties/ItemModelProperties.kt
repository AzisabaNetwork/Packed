package net.azisaba.packed.items.properties

import net.azisaba.packed.util.KeySerializer
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable
import net.kyori.adventure.key.Key

@Serializable
sealed interface PackItemModelProperties {
    val type: Key
}

@Serializable
data class PackModelItemModelProperties(
    @Serializable(with = KeySerializer::class)
    val model: Key,
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
)

@Serializable
data class PackCompositeItemModelProperties(
    val models: List<PackItemModelProperties>,
) : PackItemModelProperties {
    @EncodeDefault
    override val type: Key = Key.key("composite")
}

@Serializable
class PackEmptyItemModelProperties : PackItemModelProperties {
    @EncodeDefault
    override val type: Key = Key.key("empty")
}

@Serializable
class PackBundleSelectedItemItemModelProperties : PackItemModelProperties {
    @EncodeDefault
    override val type: Key = Key.key("bundle/selected_item")
}
