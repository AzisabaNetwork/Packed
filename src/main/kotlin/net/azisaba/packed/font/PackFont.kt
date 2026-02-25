package net.azisaba.packed.font

import net.azisaba.packed.font.provider.PackFontProvider
import kotlinx.serialization.Serializable
import net.azisaba.packed.KeyedJsonPerFileResourceType
import net.azisaba.packed.util.KeyedPackResource
import net.azisaba.packed.PackResourceType

@Serializable
data class PackFont(
    val providers: List<PackFontProvider>
) {
    internal companion object {
        fun resourceType(): PackResourceType<KeyedPackResource<PackFont>> = KeyedJsonPerFileResourceType(
            "font", serializer()
        )
    }
}
