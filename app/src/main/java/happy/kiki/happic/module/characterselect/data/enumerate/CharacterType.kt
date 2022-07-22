package happy.kiki.happic.module.characterselect.data.enumerate

import androidx.annotation.DrawableRes
import happy.kiki.happic.R
import happy.kiki.happic.module.characterselect.data.enumerate.CharacterType.Serializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

data class CharacterLevel(
    @DrawableRes val drawableRes: Int,
    val text: String,
)

@Serializable(with = Serializer::class)
enum class CharacterType(val id: Int) {
    MOON(0), CLOUD(1);

    fun stateByLevel(level: Int): CharacterLevel {
        return when (this) {
            MOON -> hashMapOf(
                1 to CharacterLevel(R.drawable.hp_img_m1, "수줍은 조각달"),
                2 to CharacterLevel(R.drawable.hp_img_m2, "점점 성장하는 반달"),
                3 to CharacterLevel(R.drawable.hp_img_m3, "어른스러운 보름달"),
                4 to CharacterLevel(R.drawable.hp_img_m4, "상담 마스터 슈퍼문"),
            )
            CLOUD -> hashMapOf(
                1 to CharacterLevel(R.drawable.hp_img_c1, "낯가리는 조각구름"),
                2 to CharacterLevel(R.drawable.hp_img_c2, "생기발랄 비구름"),
                3 to CharacterLevel(R.drawable.hp_img_c3, "장난치는 뭉게구름"),
                4 to CharacterLevel(R.drawable.hp_img_c4, "소중한 무지개구름"),
            )
        }.getOrDefault(level, CharacterLevel(R.drawable.hp_img_m1, "수줍은 조각달"))
    }

    object Serializer : KSerializer<CharacterType> {
        override val descriptor = serialDescriptor<Int>()

        override fun deserialize(decoder: Decoder) =
            decoder.decodeInt().run { values().find { it.id == this } ?: values().first() }

        override fun serialize(encoder: Encoder, value: CharacterType) = encoder.encodeInt(value.id)
    }
}