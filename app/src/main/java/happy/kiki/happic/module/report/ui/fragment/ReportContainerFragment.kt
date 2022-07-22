package happy.kiki.happic.module.report.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import happy.kiki.happic.databinding.FragmentReportContainerBinding
import happy.kiki.happic.module.core.util.AutoClearedValue
import happy.kiki.happic.module.core.util.extension.addFragment
import happy.kiki.happic.module.core.util.extension.collectFlowWhenStarted
import happy.kiki.happic.module.core.util.extension.isChildFragmentExistIn
import happy.kiki.happic.module.core.util.extension.popChildBackStack
import happy.kiki.happic.module.core.util.extension.popChildBackStacksUntilNameFound

class ReportContainerFragment : Fragment() {
    private var binding by AutoClearedValue<FragmentReportContainerBinding>()
    private val navigationVm by viewModels<ReportNavigationViewModel>()

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (isChildFragmentExistIn<ReportDetailFragment>()) {
                popChildBackStack()
            } else {
                isEnabled = false
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentReportContainerBinding.inflate(inflater, container, false).let { binding = it; it.root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)

        if (isChildFragmentExistIn<ReportFragment>()) {
            popChildBackStacksUntilNameFound(ReportFragment::class.java.simpleName)
        } else {
            addFragment<ReportFragment>(
                binding.container,
                skipAddToBackStack = true,
                fragmentManager = childFragmentManager,
                tag = ReportFragment::class.java.simpleName
            )
        }

        collectFlowWhenStarted(navigationVm.onNavigateDetail.flow) {
            onBackPressedCallback.isEnabled = true
            addFragment<ReportDetailFragment>(
                binding.container,
                fragmentManager = childFragmentManager,
                arg = it,
                tag = ReportDetailFragment::class.java.simpleName
            )
        }
    }
}