package happy.kiki.happic.module.home.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HomeDataModel(
    val characterId: Int,
    val characterName: String,
    @SerialName("growth_rate") val growthRate: Int,
    val level: Int,
    val isPosted: Boolean
)