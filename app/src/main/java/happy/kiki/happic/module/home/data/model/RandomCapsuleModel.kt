package happy.kiki.happic.module.home.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RandomCapsuleModel(
    val photo: String,
    @SerialName("when")
    val hour: Int,
    val where: String,
    val who: String,
    val what: String,
)