@file:OptIn(ExperimentalSerializationApi::class)

package happy.kiki.happic.module.report.data.enumerate

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
enum class ReportCategoryOption(val index: Int) {
    @JsonNames(names = ["hour", "#hour", "when"])
    hour(0),

    @JsonNames(names = ["where", "#where"])
    where(1),

    @JsonNames(names = ["who", "#who"])
    who(2),

    @JsonNames(names = ["what", "#what"])
    what(3)

    ;

    companion object {
        fun byIndex(index: Int) = values().getOrNull(index) ?: hour
    }
}