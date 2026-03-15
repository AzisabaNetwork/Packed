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

        fun black(): Translation = LegacyStyleTranslation('0')

        fun darkBlue(): Translation = LegacyStyleTranslation('1')

        fun darkGreen(): Translation = LegacyStyleTranslation('2')

        fun darkAqua(): Translation = LegacyStyleTranslation('3')

        fun darkRed(): Translation = LegacyStyleTranslation('4')

        fun darkPurple(): Translation = LegacyStyleTranslation('5')

        fun gold(): Translation = LegacyStyleTranslation('6')

        fun gray(): Translation = LegacyStyleTranslation('7')

        fun darkGray(): Translation = LegacyStyleTranslation('8')

        fun blue(): Translation = LegacyStyleTranslation('9')

        fun green(): Translation = LegacyStyleTranslation('a')

        fun aqua(): Translation = LegacyStyleTranslation('b')

        fun red(): Translation = LegacyStyleTranslation('c')

        fun lightPurple(): Translation = LegacyStyleTranslation('d')

        fun yellow(): Translation = LegacyStyleTranslation('e')

        fun white(): Translation = LegacyStyleTranslation('f')

        fun obfuscated(): Translation = LegacyStyleTranslation('k')

        fun bold(): Translation = LegacyStyleTranslation('l')

        fun strikethrough(): Translation = LegacyStyleTranslation('m')

        fun underline(): Translation = LegacyStyleTranslation('n')

        fun italic(): Translation = LegacyStyleTranslation('o')

        fun resetStyle(): Translation = LegacyStyleTranslation('r')
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

private data class LegacyStyleTranslation(val magic: Char) : Translation {
    override fun toString(): String = "§$magic"
}

@ApiStatus.Internal
object TranslationSerializer : PackedSerializer<Translation>() {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Translation", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Translation) {
        encoder.encodeString(value.toString())
    }
}

