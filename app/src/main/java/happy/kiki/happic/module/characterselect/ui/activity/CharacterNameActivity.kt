package happy.kiki.happic.module.characterselect.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import happy.kiki.happic.databinding.ActivityCharacterNameBinding
import happy.kiki.happic.module.core.util.extension.addLengthFilter
import happy.kiki.happic.module.core.util.extension.addNoSpaceFilter
import happy.kiki.happic.module.core.util.extension.showToast

class CharacterNameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCharacterNameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharacterNameBinding.inflate(layoutInflater).also { setContentView(it.root) }

        binding.etName.addNoSpaceFilter().addLengthFilter(5)

        initButtonClickListeners()
    }

    private fun initButtonClickListeners() {

        showToast("5글자 이내로 입력해주세요")

        binding.ibBack.setOnClickListener {
            finish()
        }
    }
}

