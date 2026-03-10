package net.azisaba.packed.items.properties

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.azisaba.packed.KeySerializer
import net.azisaba.packed.PackDirection
import net.azisaba.packed.PackDyeColor
import net.kyori.adventure.key.Key

@Serializable
sealed interface PackSpecialModel {
    val type: String
}

@Serializable
data class PackBannerSpecialModel(val color: PackDyeColor) : net.azisaba.packed.items.properties.PackSpecialModel {
    @EncodeDefault
    override val type: String = "banner"
}

@Serializable
data class PackBedSpecialModel(
    @Serializable(with = KeySerializer::class)
    val texture: Key,
) : net.azisaba.packed.items.properties.PackSpecialModel {
    @EncodeDefault
    override val type: String = "bed"
}

@Serializable
data class PackChestSpecialModel(
    @Serializable(with = KeySerializer::class)
    val texture: Key,
    val openness: Float,
) : net.azisaba.packed.items.properties.PackSpecialModel {
    @EncodeDefault
    override val type: String = "chest"
}

@Serializable
class PackConduitSpecialModel : net.azisaba.packed.items.properties.PackSpecialModel {
    @EncodeDefault
    override val type: String = "conduit"
}

@Serializable
class PackDecoratedPotSpecialModel : net.azisaba.packed.items.properties.PackSpecialModel {
    @EncodeDefault
    override val type: String = "decorated_pot"
}

@Serializable
data class PackHeadSpecialModel(
    val kind: Kind,
    @Serializable(with = KeySerializer::class)
    val texture: Key? = null,
    val animation: Float? = null,
) : net.azisaba.packed.items.properties.PackSpecialModel {
    @EncodeDefault
    override val type: String = "head"

    @Serializable
    enum class Kind {
        @SerialName("skeleton")
        SKELETON,

        @SerialName("wither_skeleton")
        WITHER_SKELETON,

        @SerialName("zombie")
        ZOMBIE,

        @SerialName("creeper")
        CREEPER,

        @SerialName("piglin")
        PIGLIN,

        @SerialName("dragon")
        DRAGON,
    }
}

@Serializable
class PackPlayerHeadSpecialModel : net.azisaba.packed.items.properties.PackSpecialModel {
    @EncodeDefault
    override val type: String = "player_head"
}

@Serializable
class PackShieldSpecialModel : net.azisaba.packed.items.properties.PackSpecialModel {
    @EncodeDefault
    override val type: String = "shield"
}

@Serializable
data class PackShulkerBoxSpecialModel(
    @Serializable(with = KeySerializer::class)
    val texture: Key,
    val openness: Float? = null,
    val orientation: PackDirection? = null,
) : net.azisaba.packed.items.properties.PackSpecialModel {
    @EncodeDefault
    override val type: String = "shulker_box"
}

@Serializable
data class PackStandingSignSpecialModel(
    val woodType: WoodType,
    val texture: Key? = null,
) : net.azisaba.packed.items.properties.PackSpecialModel {
    @EncodeDefault
    override val type: String = "standing_sign"

    @Serializable
    enum class WoodType {
        @SerialName("oak")
        OAK,

        @SerialName("spruce")
        SPRUCE,

        @SerialName("birch")
        BIRCH,

        @SerialName("acacia")
        ACACIA,

        @SerialName("cherry")
        CHERRY,

        @SerialName("jungle")
        JUNGLE,

        @SerialName("dark_oak")
        DARK_OAK,

        @SerialName("pale_oak")
        PALE_OAK,

        @SerialName("mangrove")
        MANGROVE,

        @SerialName("bamboo")
        BAMBOO,

        @SerialName("crimson")
        CRIMSON,

        @SerialName("warped")
        WARPED,
    }
}

@Serializable
data class PackHangingSignSpecialModel(
    val woodType: WoodType,
    val texture: Key? = null,
) : net.azisaba.packed.items.properties.PackSpecialModel {
    @EncodeDefault
    override val type: String = "hanging_sign"

    @Serializable
    enum class WoodType {
        @SerialName("oak")
        OAK,

        @SerialName("spruce")
        SPRUCE,

        @SerialName("birch")
        BIRCH,

        @SerialName("acacia")
        ACACIA,

        @SerialName("cherry")
        CHERRY,

        @SerialName("jungle")
        JUNGLE,

        @SerialName("dark_oak")
        DARK_OAK,

        @SerialName("pale_oak")
        PALE_OAK,

        @SerialName("mangrove")
        MANGROVE,

        @SerialName("bamboo")
        BAMBOO,

        @SerialName("crimson")
        CRIMSON,

        @SerialName("warped")
        WARPED,
    }
}

@Serializable
class PackTridentSpecialModel : net.azisaba.packed.items.properties.PackSpecialModel {
    @EncodeDefault
    override val type: String = "trident"
}
