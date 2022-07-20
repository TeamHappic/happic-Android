package happy.kiki.happic.module.characterselect.ui.activity

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import happy.kiki.happic.R
import happy.kiki.happic.databinding.ActivityCharacterSelectBinding
import happy.kiki.happic.module.characterselect.data.enumerate.CharacterType
import happy.kiki.happic.module.characterselect.data.enumerate.CharacterType.CLOUD
import happy.kiki.happic.module.characterselect.data.enumerate.CharacterType.MOON
import happy.kiki.happic.module.characterselect.ui.activity.CharacterNameActivity.Argument
import happy.kiki.happic.module.core.ui.widget.RoundButton
import happy.kiki.happic.module.core.util.debugE
import happy.kiki.happic.module.core.util.extension.argument
import happy.kiki.happic.module.core.util.extension.collectFlowWhenStarted
import happy.kiki.happic.module.core.util.extension.pushActivity
import happy.kiki.happic.module.core.util.extension.px
import happy.kiki.happic.module.core.util.extension.showToast
import kotlinx.parcelize.Parcelize

class CharacterSelectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCharacterSelectBinding

    @Parcelize
    data class Argument(val characerType: CharacterType) : Parcelable

    private val arg by argument<Argument>()
    private val vm by viewModels<CharacterSelectViewModel>()

    //    private val selectedCharacter by lazy {
    //        MutableStateFlow(arg.name)
    //    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCharacterSelectBinding.inflate(layoutInflater).also { binding = it;setContentView(it.root) }

        animateBottomContainer()
        initEvent()
        initButtonClickListeners()
    }

    private fun initEvent() {
        vm.characterType.value = arg.characerType

        binding.bvMoon.setOnClickListener {
            vm.characterType.value = MOON
        }
        binding.bvCloud.setOnClickListener {
            vm.characterType.value = CLOUD
        }

        collectFlowWhenStarted(vm.characterType) {
            debugE(vm.characterType.value)
            it?.run {
                if (it == MOON) {
                    with(binding) {
                        tvAnswer1.text = "내가 좋아하는 사람들이 행복하다고 말하는 거!"
                        tvAnswer2.text = "조용히 일기에 걱정거리를 적어"
                        tvAnswer3.text = "나는 편지와 함께 고맙다고 말해!"
                        bvMoon.strokeWidth = px(2).toFloat()
                        bvCloud.strokeWidth = 0f
                        bvCloud.foreground = ColorDrawable(getColor(R.color.bg_black2)).apply { alpha = 220 }
                        bvMoon.foreground = null
                        btnJoin.type = RoundButton.Type.DARK_BLUE
                    }
                } else {
                    with(binding) {
                        tvAnswer1.text = "내가 한 일이 완벽하게 끝나는 거!"
                        tvAnswer2.text = "걱정해서 뭐하지라고 생각하면서 그냥 잊어!"
                        tvAnswer3.text = "친구에게 맛있는 걸 사주면서 간접적으로 표현해!"
                        bvCloud.strokeWidth = px(2).toFloat()
                        bvMoon.strokeWidth = 0f
                        bvMoon.foreground = ColorDrawable(getColor(R.color.bg_black2)).apply { alpha = 220 }
                        bvCloud.foreground = null
                        btnJoin.type = RoundButton.Type.DARK_PURPLE
                    }
                }
            }
        }
    }

    private fun initButtonClickListeners() = binding.btnJoin.setOnClickListener {
        pushActivity<CharacterNameActivity>(
            CharacterNameActivity.Argument(vm.characterType.value)
        )
    }

    private fun animateBottomContainer() { //        binding.bottomContainer.translateYUp(duration = 1500)
        //        binding.bottomContainer.fadeIn(duration = 2000)
    }
}