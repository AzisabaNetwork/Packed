package net.azisaba.packed.font.provider

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.azisaba.serialization.KeySerializer
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

    object ShiftSerializer : KSerializer<Shift> {
        @OptIn(InternalSerializationApi::class)
        override val descriptor: SerialDescriptor = buildSerialDescriptor("Shift", StructureKind.LIST)

        override fun serialize(encoder: Encoder, value: Shift) {
            val composite = encoder.beginCollection(descriptor, 2)
            composite.encodeIntElement(descriptor, 0, value.horizontal)
            composite.encodeIntElement(descriptor, 1, value.vertical)
            composite.endStructure(descriptor)
        }

        override fun deserialize(decoder: Decoder): Shift {
            val compositeDecoder = decoder.beginStructure(descriptor)

            val values = if (compositeDecoder.decodeSequentially()) {
                mapOf(
                    0 to compositeDecoder.decodeIntElement(descriptor, 0),
                    1 to compositeDecoder.decodeIntElement(descriptor, 1),
                )
            } else buildMap {
                while (true) {
                    when (val index = compositeDecoder.decodeElementIndex(descriptor)) {
                        CompositeDecoder.DECODE_DONE -> break
                        0, 1 -> put(index, compositeDecoder.decodeIntElement(descriptor, index))
                        else -> throw SerializationException("Unexpected index: $index")
                    }
                }
            }

            compositeDecoder.endStructure(descriptor)

            val horizontal = values[0] ?: throw SerializationException("Missing 'horizontal'")
            val vertical = values[1] ?: throw SerializationException("Missing 'vertical'")

            return Shift(horizontal, vertical)
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
