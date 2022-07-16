package happy.kiki.happic.module.report.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ReportByMonthlyModel(
    val month: Int, val count: Int, val dates: List<Int>
)