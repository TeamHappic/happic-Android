package happy.kiki.happic.module.characterselect.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import happy.kiki.happic.databinding.ActivityCharacterBinding
import happy.kiki.happic.module.characterselect.ui.activity.CharacterSelectActivity.Argument
import happy.kiki.happic.module.core.util.extension.pushActivity
import android.util.Pair as UtilPair

class CharacterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCharacterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharacterBinding.inflate(layoutInflater).also { setContentView(it.root) }

        val sharedElementPairs = listOf(
            UtilPair(binding.bvMoon as View, "moon"), UtilPair(binding.bvCloud as View, "cloud")
        )

        binding.bvMoon.setOnClickListener {
            pushActivity<CharacterSelectActivity>(
                Argument("moon"), sharedElementPairs = sharedElementPairs
            )
        }

        binding.bvCloud.setOnClickListener {
            pushActivity<CharacterSelectActivity>(
                Argument("cloud"), sharedElementPairs = sharedElementPairs
            )
        }
    }
}