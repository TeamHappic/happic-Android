package happy.kiki.happic.module.characterselect.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import happy.kiki.happic.databinding.ActivityCharacterBinding
import happy.kiki.happic.module.characterselect.ui.activity.CharacterSelectActivity.Argument
import happy.kiki.happic.module.core.util.extension.addFragment
import happy.kiki.happic.module.core.util.extension.pushActivity
import happy.kiki.happic.module.core.util.extension.replaceActivity

class CharacterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCharacterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharacterBinding.inflate(layoutInflater).also { setContentView(it.root) }

        binding.bvMoon.setOnClickListener {
            pushActivity<CharacterSelectActivity>(Argument("moon"))
        }

        binding.bvCloud.setOnClickListener {
            pushActivity<CharacterSelectActivity>(Argument("cloud"))
        }
    }
}