package net.azisaba.packed.lang

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.kyori.adventure.translation.Translatable
import org.jetbrains.annotations.ApiStatus

@Serializable(with = TranslationKeySerializer::class)
sealed interface TranslationKey : Translatable {
    companion object {
        fun key(key: String): TranslationKey = TranslationKeyImpl(key)
    }
}

private data class TranslationKeyImpl(val key: String) : TranslationKey {
    override fun translationKey(): String = key
}

@ApiStatus.Internal
object TranslationKeySerializer : KSerializer<TranslationKey> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("TranslationKey", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: TranslationKey) {
        encoder.encodeString(value.translationKey())
    }

    override fun deserialize(decoder: Decoder): TranslationKey {
        return TranslationKeyImpl(decoder.decodeString())
    }
}
