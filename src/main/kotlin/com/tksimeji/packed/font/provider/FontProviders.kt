package com.tksimeji.packed.font.provider

import com.tksimeji.packed.KeySerializer
import com.tksimeji.packed.PackedSerializer
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Encoder
import net.kyori.adventure.key.Key

@Serializable
sealed interface PackFontProvider {
    val type: String

    val filter: PackFontFilter?
}

@Serializable
data class PackBitmapFontProvider(
    @Serializable(with = KeySerializer::class)
    val file: Key,
    val chars: List<String>,
    val ascent: Int,
    val height: Int,
    override val filter: PackFontFilter? = null,
) : PackFontProvider {
    @EncodeDefault
    override val type: String = "bitmap"
}

@Serializable
data class PackReferenceFontProvider(
    @Serializable(with = KeySerializer::class)
    val id: Key,
    override val filter: PackFontFilter? = null,
) : PackFontProvider {
    @EncodeDefault
    override val type: String = "reference"
}

@Serializable
data class PackSpaceFontProvider(
    val advances: Map<Char, Int>,
    override val filter: PackFontFilter? = null,
) : PackFontProvider {
    @EncodeDefault
    override val type: String = "space"
}

@Serializable
data class PackTtfFontProvider(
    @Serializable(with = KeySerializer::class)
    val file: Key,
    val size: Int,
    val oversample: Int,
    val shift: Shift = Shift(0, 0),
    val skip: List<Char> = emptyList(),
    override val filter: PackFontFilter? = null,
) : PackFontProvider {
    @EncodeDefault
    override val type: String = "ttf"

    @Serializable(with = ShiftSerializer::class)
    data class Shift(val horizontal: Int, val vertical: Int)

    private object ShiftSerializer : PackedSerializer<Shift>() {
        @OptIn(InternalSerializationApi::class)
        override val descriptor: SerialDescriptor = buildSerialDescriptor("Shift", StructureKind.LIST)

        override fun serialize(encoder: Encoder, value: Shift) {
            val composite = encoder.beginCollection(descriptor, 2)
            composite.encodeIntElement(descriptor, 0, value.horizontal)
            composite.encodeIntElement(descriptor, 1, value.vertical)
            composite.endStructure(descriptor)
        }
    }
}

@Serializable
data class PackUnihexFontProvider(
    @Serializable(with = KeySerializer::class)
    val hexFile: Key,
    val sizeOverrides: List<SizeOverride> = emptyList(),
    override val filter: PackFontFilter? = null,
) : PackFontProvider {
    @EncodeDefault
    override val type: String = "unihex"

    @Serializable
    data class SizeOverride(
        val from: String,
        val to: String,
        val left: Int,
        val right: Int,
    )
}
