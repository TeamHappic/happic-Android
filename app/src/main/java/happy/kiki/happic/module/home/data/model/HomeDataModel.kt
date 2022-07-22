package happy.kiki.happic.module.home.data.model

import happy.kiki.happic.module.characterselect.data.enumerate.CharacterType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HomeDataModel(
    val characterId: CharacterType,
    val characterName: String,
    @SerialName("growth_rate") val growthRate: Int,
    val level: Int,
    val isPosted: Boolean
)