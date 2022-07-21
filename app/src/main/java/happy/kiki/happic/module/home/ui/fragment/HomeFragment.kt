import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import happy.kiki.happic.R
import happy.kiki.happic.databinding.FragmentHomeBinding
import happy.kiki.happic.module.characterselect.data.enumerate.CharacterType.MOON
import happy.kiki.happic.module.core.util.AutoCleardValue
import happy.kiki.happic.module.core.util.extension.collectFlowWhenStarted
import happy.kiki.happic.module.core.util.extension.pushActivity
import happy.kiki.happic.module.home.ui.fragment.HomeViewModel
import happy.kiki.happic.module.upload.ui.activity.UploadHappicActivity

class HomeFragment : Fragment() {
    private var binding by AutoCleardValue<FragmentHomeBinding>()

    private val vm by viewModels<HomeViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentHomeBinding.inflate(inflater, container, false).let { binding = it; it.root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initEvent()
        uploadButtonClickListener()
    }

    private fun initEvent() {
        collectFlowWhenStarted(vm.characterName) {
            binding.tvCharacterName.text = vm.characterName.value
        }

        collectFlowWhenStarted(vm.characterType) {
            it?.run {
                if (it == MOON) {
                    with(binding) {
                        ivCharacter.setImageResource(R.drawable.character_moon)
                    }
                } else {
                    with(binding) {
                        ivCharacter.setImageResource(R.drawable.character_cloud)
                    }
                }
            }
        }

        collectFlowWhenStarted(vm.isPosted) {
            it?.run {
                binding.btnUpload.isVisible = !it
            }
        }

        collectFlowWhenStarted(vm.growthRate) {
            it?.run {
                with(binding){
                    progressbarLine.progress = it.toInt() * 10
                    progressbarLine.max = 60
                }
            }
        }

        collectFlowWhenStarted(vm.level) {
            it?.run {
                if (vm.characterType.value == MOON) {
                    binding.tvLevel.text = when (it) {
                        "1" -> "Lv.1 수줍은 조각달"
                        "2" -> "Lv.2 점점 성장하는 반달"
                        "3" -> "Lv.3 어른스러운 보름달"
                        else -> "Lv.4 상담 마스터 슈퍼문"
                    }
                } else {
                    binding.tvLevel.text = when (it) {
                        "1" -> "Lv.1 낯가리는 조각구름"
                        "2" -> "Lv.2 생기발랄 비구름"
                        "3" -> "Lv.3 장난치는 뭉게구름"
                        else -> "Lv.4 소중한 무지개구름"
                    }
                }
                binding.tvLevel2.text = when (it){
                    "1" -> "1/6"
                    "2" -> "2/6"
                    "3" -> "3/6"
                    else -> "4/6"
                }
            }
        }
    }

    private fun uploadButtonClickListener() = binding.btnUpload.setOnClickListener {
        pushActivity<UploadHappicActivity>()
    }
}