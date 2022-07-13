package happy.kiki.happic.module.characterselect.ui.activity

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doBeforeTextChanged
import androidx.core.widget.doOnTextChanged
import happy.kiki.happic.databinding.ActivityCharacterNameBinding
import happy.kiki.happic.module.core.util.extension.collectFlowWhenStarted
import kotlinx.coroutines.flow.MutableStateFlow

class CharacterNameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCharacterNameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharacterNameBinding.inflate(layoutInflater).also { setContentView(it.root) }

        binding.etName.doOnTextChanged { text, start, before, count ->
            val next = text?.trim()?.filter { it != ' ' } ?: ""
            if(text != next) {
                binding.etName.setText(next)
                binding.etName.setSelection(next.length)
            }
        }
        initButtonClickListeners()
    }

    private fun initButtonClickListeners() {

        showToast("5글자 이내로 입력해주세요")

        binding.ibBack.setOnClickListener {
            finish()
        }
    }

    fun Context.showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

}

