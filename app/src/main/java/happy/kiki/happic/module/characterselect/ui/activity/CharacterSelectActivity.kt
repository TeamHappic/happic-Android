package happy.kiki.happic.module.characterselect.ui.activity

import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import happy.kiki.happic.databinding.ActivityCharacterSelectBinding
import happy.kiki.happic.module.core.util.extension.argument
import happy.kiki.happic.module.core.util.extension.fadeIn
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
    }

    private fun animateBottomContainer() {
//        binding.bottomContainer.translateYUp(duration = 1500)
//        binding.bottomContainer.fadeIn(duration = 2000)
    }
}