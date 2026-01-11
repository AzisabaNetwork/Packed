package com.tksimeji.packed.items.properties

import com.tksimeji.packed.EnumSerializer
import com.tksimeji.packed.KeySerializer
import com.tksimeji.packed.PackDirection
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.kyori.adventure.key.Key
import org.bukkit.DyeColor

@Serializable
sealed interface PackSpecialModel {
    val type: String
}

@Serializable
data class PackBannerSpecialModel(
    @Serializable(with = DyeColorSerializer::class)
    val color: DyeColor,
) : PackSpecialModel {
    @EncodeDefault
    override val type: String = "banner"
}

@Serializable
data class PackBedSpecialModel(
    @Serializable(with = KeySerializer::class)
    val texture: Key,
) : PackSpecialModel {
    @EncodeDefault
    override val type: String = "bed"
}

@Serializable
data class PackChestSpecialModel(
    @Serializable(with = KeySerializer::class)
    val texture: Key,
    val openness: Float,
) : PackSpecialModel {
    @EncodeDefault
    override val type: String = "chest"
}

@Serializable
class PackConduitSpecialModel : PackSpecialModel {
    @EncodeDefault
    override val type: String = "conduit"
}

@Serializable
class PackDecoratedPotSpecialModel : PackSpecialModel {
    @EncodeDefault
    override val type: String = "decorated_pot"
}

@Serializable
data class PackHeadSpecialModel(
    val kind: Kind,
    @Serializable(with = KeySerializer::class)
    val texture: Key? = null,
    val animation: Float? = null,
) : PackSpecialModel {
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
class PackPlayerHeadSpecialModel : PackSpecialModel {
    @EncodeDefault
    override val type: String = "player_head"
}

@Serializable
class PackShieldSpecialModel : PackSpecialModel {
    @EncodeDefault
    override val type: String = "shield"
}

@Serializable
data class PackShulkerBoxSpecialModel(
    @Serializable(with = KeySerializer::class)
    val texture: Key,
    val openness: Float? = null,
    val orientation: PackDirection? = null,
) : PackSpecialModel {
    @EncodeDefault
    override val type: String = "shulker_box"
}

@Serializable
data class PackStandingSignSpecialModel(
    val woodType: WoodType,
    val texture: Key? = null,
) : PackSpecialModel {
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
) : PackSpecialModel {
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
class PackTridentSpecialModel : PackSpecialModel {
    @EncodeDefault
    override val type: String = "trident"
}

private object DyeColorSerializer : EnumSerializer<DyeColor>(DyeColor::class)
