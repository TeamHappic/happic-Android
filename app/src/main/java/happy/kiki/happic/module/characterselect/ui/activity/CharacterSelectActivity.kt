package happy.kiki.happic.module.characterselect.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import happy.kiki.happic.databinding.ActivityCharacterSelectBinding
import happy.kiki.happic.module.core.util.debugE
import happy.kiki.happic.module.core.util.extension.argument
import kotlinx.parcelize.Parcelize

class CharacterSelectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCharacterSelectBinding

    @Parcelize
    data class Argument(val name: String) : Parcelable

    private val arg by argument<Argument>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCharacterSelectBinding.inflate(layoutInflater).also { binding = it;setContentView(it.root) }

        debugE(arg)

    }
}