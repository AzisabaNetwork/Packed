package net.azisaba.packed

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.kyori.adventure.key.Key
import net.kyori.adventure.util.RGBLike
import org.joml.Vector3ic
import org.joml.Vector4fc
import kotlin.reflect.KClass

internal abstract class PackedSerializer<T> : KSerializer<T> {
    override fun deserialize(decoder: Decoder): T =
        throw UnsupportedOperationException("Packed only supports serialization, not deserialization.")
}

internal abstract class EnumSerializer<T : Enum<T>>(private val kClass: KClass<T>) : PackedSerializer<T>() {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(kClass.simpleName ?: "Enum", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: T) {
        val snakeCase = value.name
            .replace(Regex("([a-z0-9])([A-Z])"), "$1_$2")
            .lowercase()
        encoder.encodeString(snakeCase)
    }
}

internal object KeySerializer : PackedSerializer<Key>() {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Key", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Key) {
        encoder.encodeString("${value.namespace()}:${value.value()}")
    }
}

internal object RGBLikeSerializer : PackedSerializer<RGBLike>() {
    @OptIn(InternalSerializationApi::class)
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("RGBLike", PrimitiveKind.INT)

    override fun serialize(encoder: Encoder, value: RGBLike) {
        val red = value.red()
        val green = value.green()
        val blue = value.blue()

        require(red in 0..255 && green in 0..255 && blue in 0..255) {
            "RGB must be 0..255"
        }

        encoder.encodeInt((red shl 16) or (green shl 8) or blue)
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
