package net.azisaba.packed

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
abstract class PackedSerializer<T> : KSerializer<T> {
    override fun deserialize(decoder: Decoder): T =
        throw UnsupportedOperationException("Packed only supports serialization, not deserialization.")
}

@ApiStatus.Internal
object PackedKeySerializer : PackedSerializer<PackedKey<*>>() {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("PackedKey", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: PackedKey<*>) {
        encoder.encodeString("${value.namespace()}:${value.value()}")
    }
}

@ApiStatus.Internal
object PackFormatSerializer : PackedSerializer<PackFormat>() {
    @OptIn(InternalSerializationApi::class)
    override val descriptor: SerialDescriptor = buildSerialDescriptor("PackFormat", StructureKind.LIST)

    override fun serialize(encoder: Encoder, value: PackFormat) {
        val composite = encoder.beginCollection(descriptor, 2)
        composite.encodeIntElement(descriptor, 0, value.major)
        composite.encodeIntElement(descriptor, 1, value.minor)
        composite.endStructure(descriptor)
    }
}
