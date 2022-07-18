package happy.kiki.happic.module.report.data.model

import happy.kiki.happic.module.report.data.enumerate.ReportCategoryOption
import kotlinx.serialization.Serializable

@Serializable
data class ReportHomeModel(
    val rank1s: List<Rank1>, val rank2s: List<Rank2>, val rank3s: List<Rank3>, val rank4s: Rank4
) {
    @Serializable
    data class Rank1(
        val content: String
    )

    @Serializable
    data class Rank2(
        val content: String, val category: ReportCategoryOption, val count: Int
    )

    @Serializable
    data class Rank3(
        val content: String, val images: List<String>, val count: Int
    )

    @Serializable
    data class Rank4(
        val month: Int, val count: Int
    )
}