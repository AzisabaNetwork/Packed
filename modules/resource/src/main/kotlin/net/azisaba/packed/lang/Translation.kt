package net.azisaba.packed.lang

import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Encoder
import net.azisaba.packed.PackedSerializer
import org.jetbrains.annotations.ApiStatus

@Serializable(with = TranslationSerializer::class)
sealed interface Translation {
    fun append(translation: Translation): Translation = CompositeTranslation(listOf(this, translation))

    operator fun plus(translation: Translation): Translation = append(translation)

    companion object {
        fun literal(literal: String): Translation = LiteralTranslation(literal)

        fun placeholder(): Translation = PlaceholderTranslation

        fun placeholder(index: Int): Translation = IndexedPlaceholderTranslation(index)
    }
}

private data class CompositeTranslation(val parts: List<Translation>) : Translation {
    override fun append(translation: Translation): Translation = when (translation) {
        is CompositeTranslation -> CompositeTranslation(parts + translation.parts)
        else -> CompositeTranslation(parts + translation)
    }

    override fun toString(): String = parts.joinToString("") { it.toString() }
}

private data class LiteralTranslation(val literal: String) : Translation {
    override fun toString(): String = literal
}

private object PlaceholderTranslation : Translation {
    override fun toString(): String = "%s"
}

private data class IndexedPlaceholderTranslation(val index: Int) : Translation {
    override fun toString(): String = $$"%$$index$s"
}

@ApiStatus.Internal
object TranslationSerializer : PackedSerializer<Translation>() {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Translation", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Translation) {
        encoder.encodeString(value.toString())
    }
}

