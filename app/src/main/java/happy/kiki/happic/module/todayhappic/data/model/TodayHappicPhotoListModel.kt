package happy.kiki.happic.module.todayhappic.data.model

import kotlinx.serialization.Serializable

@Serializable
data class TodayHappicPhotoListModel(
    val id: String, val day: String, val thumbnail: String
)