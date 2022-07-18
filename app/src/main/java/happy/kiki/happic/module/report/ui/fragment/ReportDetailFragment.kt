package happy.kiki.happic.module.report.ui.fragment

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import happy.kiki.happic.databinding.FragmentReportDetailBinding
import happy.kiki.happic.module.core.util.AutoCleardValue
import happy.kiki.happic.module.core.util.extension.argument
import happy.kiki.happic.module.report.data.enumerate.ReportCategoryOption
import happy.kiki.happic.module.report.data.enumerate.ReportCategoryOption.hour
import kotlinx.parcelize.Parcelize

class ReportDetailFragment : Fragment() {
    @Parcelize
    data class Argument(val tabIndex: Int, val category: ReportCategoryOption = hour) : Parcelable

    private var binding by AutoCleardValue<FragmentReportDetailBinding>()

    private val arg by argument<Argument>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentReportDetailBinding.inflate(inflater, container, false).let { binding = it; it.root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }
}