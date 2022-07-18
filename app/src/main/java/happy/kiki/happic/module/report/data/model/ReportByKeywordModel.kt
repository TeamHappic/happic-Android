package happy.kiki.happic.module.report.data.model

import happy.kiki.happic.module.report.data.enumerate.ReportCategoryOption
import kotlinx.serialization.Serializable

@Serializable
data class ReportByKeywordModel(
    val content: String, val category: ReportCategoryOption, val count: Int
)