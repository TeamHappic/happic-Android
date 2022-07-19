package happy.kiki.happic.module.todayhappic.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TodayHappicTagModel(
    val id: String,
    val date: String,
    @SerialName("when") val when1: String,
    val `where`: String,
    val who: String,
    val what: String
)