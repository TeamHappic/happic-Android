package happy.kiki.happic.module.home.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RandomCapsuleModel(
    val photo: String,
    val hour: String,
    val where: String,
    val who: String,
    val what: String,
)