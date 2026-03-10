package net.azisaba.packed.items.properties

import net.azisaba.packed.KeySerializer
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
    val tints: List<net.azisaba.packed.items.properties.PackTintSource>? = null,
) : net.azisaba.packed.items.properties.PackItemModelProperties {
    @EncodeDefault
    @Serializable(with = KeySerializer::class)
    override val type: Key = Key.key("model")
}

@Serializable
data class PackSpecialItemModelProperties(
    @Serializable(with = KeySerializer::class)
    val base: Key,
    val model: net.azisaba.packed.items.properties.PackSpecialModel,
)

@Serializable
data class PackCompositeItemModelProperties(
    val models: List<net.azisaba.packed.items.properties.PackItemModelProperties>,
) : net.azisaba.packed.items.properties.PackItemModelProperties {
    @EncodeDefault
    override val type: Key = Key.key("composite")
}

@Serializable
class PackEmptyItemModelProperties : net.azisaba.packed.items.properties.PackItemModelProperties {
    @EncodeDefault
    override val type: Key = Key.key("empty")
}

@Serializable
class PackBundleSelectedItemItemModelProperties : net.azisaba.packed.items.properties.PackItemModelProperties {
    @EncodeDefault
    override val type: Key = Key.key("bundle/selected_item")
}
