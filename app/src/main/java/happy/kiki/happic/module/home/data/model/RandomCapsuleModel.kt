package happy.kiki.happic.module.home.data.model

import android.os.Parcelable
import happy.kiki.happic.module.dailyhappic.data.model.LocalDateTimeSerializer
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
@Parcelize
data class RandomCapsuleModel(
    val photo: String,
    @Serializable(with = LocalDateTimeSerializer::class) val date: LocalDateTime,
    @SerialName("when") val hour: Int,
    val where: String,
    val who: String,
    val what: String,
) : Parcelable