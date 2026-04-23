package net.azisaba.packed

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.kyori.adventure.key.Key
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
object PackedKeySerializer : KSerializer<PackedKey<*>> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("PackedKey", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: PackedKey<*>) {
        encoder.encodeString("${value.namespace()}:${value.value()}")
    }

    override fun deserialize(decoder: Decoder): PackedKey<*> {
        return PackedKey.key<Any>(Key.key(decoder.decodeString()))
    }
}

@ApiStatus.Internal
object PackFormatSerializer : KSerializer<PackFormat> {
    @OptIn(InternalSerializationApi::class)
    override val descriptor: SerialDescriptor = buildSerialDescriptor("PackFormat", StructureKind.LIST)

    override fun serialize(encoder: Encoder, value: PackFormat) {
        val composite = encoder.beginCollection(descriptor, 2)
        composite.encodeIntElement(descriptor, 0, value.major)
        composite.encodeIntElement(descriptor, 1, value.minor)
        composite.endStructure(descriptor)
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): PackFormat {
        try {
            val major = decoder.decodeInt()
            return PackFormat(major)
        } catch (_: SerializationException) {
        }

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

        val major = values[0] ?: throw SerializationException("Missing 'major'")
        val minor = values[1] ?: throw SerializationException("Missing 'minor'")

        return PackFormat(major, minor)
    }
}
