package happy.kiki.happic.module.dailyhappic.data.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializable
data class DailyHappicModel(
    val id: String,
    val day: Int,
    val photo: String,
    val thumbnail: String,
    @SerialName("when") val hour: Int,
    val where: String,
    val who: String,
    val what: String
)

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = LocalDateTime::class)
object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    override val descriptor = serialDescriptor<LocalDateTime>()

    override fun deserialize(decoder: Decoder): LocalDateTime {
        val str = decoder.decodeString()
        DateTimeFormatter.RFC_1123_DATE_TIME
        return LocalDateTime.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-ddTHH:mm:ss.sssZ"))
    }

    override fun serialize(encoder: Encoder, value: LocalDateTime) { // fixme
        encoder.encodeString("?")
    }
}