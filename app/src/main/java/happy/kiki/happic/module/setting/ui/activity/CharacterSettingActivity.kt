package happy.kiki.happic.module.setting.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Pair
import android.view.View
import happy.kiki.happic.databinding.ActivityCharacterSettingBinding
import happy.kiki.happic.module.characterselect.data.enumerate.CharacterType.CLOUD
import happy.kiki.happic.module.characterselect.data.enumerate.CharacterType.MOON
import happy.kiki.happic.module.characterselect.ui.activity.CharacterSelectActivity
import happy.kiki.happic.module.characterselect.ui.activity.CharacterSelectActivity.Argument
import happy.kiki.happic.module.core.util.extension.pushActivity

class CharacterSettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCharacterSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharacterSettingBinding.inflate(layoutInflater).also { setContentView(it.root) }

        initBackButtonClickListeners()
        initCharacterButtonClickListeners()
    }

    private fun initBackButtonClickListeners() {
        binding.ibBack.setOnClickListener {
            finish()
        }
    }

    private fun initCharacterButtonClickListeners(){

        val sharedElementPairs = listOf(
            Pair(binding.bvMoon as View, "moon"), Pair(binding.bvCloud as View, "cloud")
        )

        binding.bvMoon.setOnClickListener {
            pushActivity<CharacterSelectActivity>(
                Argument(MOON), sharedElementPairs = sharedElementPairs
            )
        }

        binding.bvCloud.setOnClickListener {
            pushActivity<CharacterSelectActivity>(
                Argument(CLOUD), sharedElementPairs = sharedElementPairs
            )
        }
    }
}