import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import happy.kiki.happic.databinding.FragmentHomeBinding
import happy.kiki.happic.module.core.util.AutoClearedValue
import happy.kiki.happic.module.core.util.extension.collectFlowWhenStarted
import happy.kiki.happic.module.core.util.extension.isChildFragmentExistIn
import happy.kiki.happic.module.dailyhappic.ui.fragment.DailyHappicViewModel
import happy.kiki.happic.module.home.ui.dialog.RandomCapsuleBottomSheetDialog
import happy.kiki.happic.module.home.ui.fragment.HomeViewModel
import kotlinx.coroutines.delay

class HomeFragment : Fragment() {
    private var binding by AutoClearedValue<FragmentHomeBinding>()

    private val vm by viewModels<HomeViewModel>()
    private val dailyHappicViewModel by activityViewModels<DailyHappicViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentHomeBinding.inflate(inflater, container, false).let { binding = it; it.root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initEvent()
        uploadButtonClickListener()
        collectRandomCapsule()
    }

    private fun initEvent() {
        collectFlowWhenStarted(vm.homeApi.data) {
            it?.let { data ->
                binding.tvCharacterName.text = it.characterName

                val state = it.characterId.stateByLevel(it.level)

                binding.ivCharacter.setImageResource(state.drawableRes)
                binding.tvLevel.text = "Lv.${it.level} ${state.text}"
                binding.tvLevel2.text = "${it.growthRate}/6"
                binding.btnUpload.isVisible = !it.isPosted

                delay(200)
                binding.progressbarLine.max = 6
                binding.progressbarLine.setProgress(it.growthRate, true)
            }
        }

        collectFlowWhenStarted(vm.homeApi.isLoading) {
            binding.dataContainer.scheduleLayoutAnimation()
            binding.dataContainer.isVisible = !it
            binding.loading.isVisible = it
        }
    }

    private fun uploadButtonClickListener() = binding.btnUpload.setOnClickListener {
        dailyHappicViewModel.navigateUploadApi.call()
    }

    private fun collectRandomCapsule() {
        collectFlowWhenStarted(vm.onRandomCapsuleScreenOpened.flow) {
            if (!isChildFragmentExistIn<RandomCapsuleBottomSheetDialog>()) RandomCapsuleBottomSheetDialog.newInstance(it)
                .show(childFragmentManager, null)
        }
    }

    override fun onResume() {
        super.onResume()
        vm.homeApi.call()
        vm.randomCapsuleApi.call()
    }
}