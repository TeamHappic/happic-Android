package happy.kiki.happic.module.report.data.enumerate

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ReportCategoryOption(val index: Int) {
    @SerialName("when")
    whenn(0),

    @SerialName("where")
    where(1),

    @SerialName("who")
    who(2),

    @SerialName("what")
    what(3);
}