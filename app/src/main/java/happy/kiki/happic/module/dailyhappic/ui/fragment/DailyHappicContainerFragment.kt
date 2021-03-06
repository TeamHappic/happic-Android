package happy.kiki.happic.module.dailyhappic.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import happy.kiki.happic.databinding.FragmentDailyHappicContainerBinding
import happy.kiki.happic.module.core.util.AutoClearedValue
import happy.kiki.happic.module.core.util.extension.addFragment
import happy.kiki.happic.module.core.util.extension.collectFlowWhenStarted
import happy.kiki.happic.module.core.util.extension.isChildFragmentExistIn
import happy.kiki.happic.module.core.util.extension.popChildBackStack

class DailyHappicContainerFragment : Fragment() {
    private var binding by AutoClearedValue<FragmentDailyHappicContainerBinding>()

    private val vm by activityViewModels<DailyHappicViewModel>()
    private val navigationVm by activityViewModels<DailyHappicNavigationViewModel>()

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (isChildFragmentExistIn<DailyHappicDetailFragment>()) {
                vm.fetchDailyHappics()
                popChildBackStack()
            } else {
                isEnabled = false
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentDailyHappicContainerBinding.inflate(inflater, container, false).let { binding = it; it.root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)

        if (savedInstanceState == null) {
            addFragment<DailyHappicFragment>(
                binding.container,
                skipAddToBackStack = true,
                fragmentManager = childFragmentManager,
                tag = DailyHappicFragment::class.java.simpleName
            )
        }

        collectFlowWhenStarted(navigationVm.onNavigateDetail.flow) {
            onBackPressedCallback.isEnabled = true
            addFragment<DailyHappicDetailFragment>(
                binding.container,
                fragmentManager = childFragmentManager,
                tag = DailyHappicDetailFragment::class.java.simpleName
            )
        }
    }

    override fun onResume() {
        super.onResume()
        vm.fetchDailyHappics()
    }
}