package happy.kiki.happic.module.dailyhappic.data.model

import kotlinx.serialization.Serializable

@Serializable
data class DailyHappicPhotoListModel(
    val id: String, val day: String, val thumbnail: String
)