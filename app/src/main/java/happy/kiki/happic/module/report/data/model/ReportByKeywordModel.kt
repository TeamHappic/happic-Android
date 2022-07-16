package happy.kiki.happic.module.report.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ReportByKeywordModel(
    val content: String, val category: String, val count: Int
)