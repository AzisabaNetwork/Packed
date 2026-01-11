package com.tksimeji.packed

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.kyori.adventure.key.Key
import org.joml.Vector3ic
import org.joml.Vector4fc

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

internal object Vector3icSerializer : PackedSerializer<Vector3ic>() {
    @OptIn(InternalSerializationApi::class)
    override val descriptor: SerialDescriptor = buildSerialDescriptor("Vector3ic", StructureKind.LIST)

    override fun serialize(encoder: Encoder, value: Vector3ic) {
        val composite = encoder.beginCollection(descriptor, 3)
        composite.encodeIntElement(descriptor, 0, value.x())
        composite.encodeIntElement(descriptor, 1, value.y())
        composite.encodeIntElement(descriptor, 2, value.z())
        composite.endStructure(descriptor)
    }
}

internal object Vector4fcSerializer : PackedSerializer<Vector4fc>() {
    @OptIn(InternalSerializationApi::class)
    override val descriptor: SerialDescriptor = buildSerialDescriptor("Vector3fc", StructureKind.LIST)

    override fun serialize(encoder: Encoder, value: Vector4fc) {
        val composite = encoder.beginCollection(descriptor, 4)
        composite.encodeFloatElement(descriptor, 0, value.x())
        composite.encodeFloatElement(descriptor, 1, value.y())
        composite.encodeFloatElement(descriptor, 2, value.z())
        composite.encodeFloatElement(descriptor, 3, value.w())
        composite.endStructure(descriptor)
    }
}
