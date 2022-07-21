package happy.kiki.happic.module.characterselect.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import happy.kiki.happic.databinding.ActivityCharacterBinding
import happy.kiki.happic.module.characterselect.data.enumerate.CharacterType
import happy.kiki.happic.module.characterselect.data.enumerate.CharacterType.CLOUD
import happy.kiki.happic.module.characterselect.data.enumerate.CharacterType.MOON
import happy.kiki.happic.module.characterselect.provider.CharacterSelectFlowProvider
import happy.kiki.happic.module.core.util.extension.pushActivity
import android.util.Pair as UtilPair

class CharacterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCharacterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharacterBinding.inflate(layoutInflater).also { setContentView(it.root) }

        initCharacterButtonClickListeners()
    }

    private fun initCharacterButtonClickListeners() {
        val sharedElementPairs = listOf(
            UtilPair(binding.bvMoon as View, "moon"), UtilPair(binding.bvCloud as View, "cloud")
        )

        fun setCharacterAndNavigate(type: CharacterType) {
            CharacterSelectFlowProvider.character.value = type
            pushActivity<CharacterSelectActivity>(
                sharedElementPairs = sharedElementPairs
            )
        }

        binding.bvMoon.setOnClickListener { setCharacterAndNavigate(MOON) }
        binding.bvCloud.setOnClickListener { setCharacterAndNavigate(CLOUD) }
    }
}