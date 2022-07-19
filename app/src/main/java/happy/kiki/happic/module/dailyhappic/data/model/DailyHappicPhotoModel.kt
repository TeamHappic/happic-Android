package happy.kiki.happic.module.dailyhappic.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DailyHappicPhotoModel(
    val id: String,
    val leftId: String,
    val rightId: String,
    val date: String,
    val photo: String,
    @SerialName("when") val when1: String,
    val where: String,
    val who: String,
    val what: String
)