package happy.kiki.happic.module.report.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ReportByCategoryModel(
    val content: String, val images: List<String>, val count: Int
)