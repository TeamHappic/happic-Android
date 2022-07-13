package happy.kiki.happic.module.characterselect.ui.activity

import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import happy.kiki.happic.databinding.ActivityCharacterSelectBinding
import happy.kiki.happic.module.core.util.extension.argument
import happy.kiki.happic.module.core.util.extension.fadeIn
import happy.kiki.happic.module.core.util.extension.pushActivity
import happy.kiki.happic.module.core.util.extension.translateYUp
import kotlinx.parcelize.Parcelize

class CharacterSelectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCharacterSelectBinding

    @Parcelize
    data class Argument(val name: String) : Parcelable

    private val arg by argument<Argument>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCharacterSelectBinding.inflate(layoutInflater).also { binding = it;setContentView(it.root) }

        animateBottomContainer()
        initEvent()
        initButtonClickListeners()

    }

    private fun initEvent(){
        if(arg.name == "cloud"){
            binding.tvAnswer1.setText("내가 한 일이 완벽하게 끝나는 거!")
            binding.tvAnswer2.setText("걱정해서 뭐하지라고 생각하면서 그냥 잊어!")
            binding.tvAnswer3.setText("친구에게 맛있는 걸 사주면서 간접적으로 표현해!")
        }
    }

    private fun initButtonClickListeners(){
        binding.btnJoin.setOnClickListener {
            if (arg.name == "moon") {
                pushActivity<CharacterNameActivity>(Argument("moon"))
            }else {
                pushActivity<CharacterNameActivity>(Argument("cloud"))
            }
        }
    }

    private fun animateBottomContainer() {
//        binding.bottomContainer.translateYUp(duration = 1500)
//        binding.bottomContainer.fadeIn(duration = 2000)
    }
}