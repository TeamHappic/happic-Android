package happy.kiki.happic.module.characterselect.data.enumerate

import happy.kiki.happic.module.characterselect.data.enumerate.CharacterType.Serializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = Serializer::class)
enum class CharacterType(val id: Int) {
    MOON(0), CLOUD(1);

    object Serializer : KSerializer<CharacterType> {
        override val descriptor = serialDescriptor<Int>()

        override fun deserialize(decoder: Decoder) =
            decoder.decodeInt().run { values().find { it.id == this } ?: values().first() }

        override fun serialize(encoder: Encoder, value: CharacterType) = encoder.encodeInt(value.id)
    }
}