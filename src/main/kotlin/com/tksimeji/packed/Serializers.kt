package com.tksimeji.packed

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.kyori.adventure.key.Key

internal abstract class PackedSerializer<T> : KSerializer<T> {
    override fun deserialize(decoder: Decoder): T =
        throw UnsupportedOperationException("Packed only supports serialization, not deserialization.")
}

internal object KeySerializer : PackedSerializer<Key>() {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Key", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Key) {
        encoder.encodeString("${value.namespace()}:${value.value()}")
    }
}