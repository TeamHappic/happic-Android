package happy.kiki.happic.module.characterselect.ui.activity

import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import happy.kiki.happic.R
import happy.kiki.happic.databinding.ActivityCharacterNameBinding
import happy.kiki.happic.module.core.util.extension.addLengthFilter
import happy.kiki.happic.module.core.util.extension.addNoSpaceFilter
import happy.kiki.happic.module.core.util.extension.argument
import happy.kiki.happic.module.core.util.extension.collectFlowWhenStarted
import happy.kiki.happic.module.core.util.extension.showToast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.parcelize.Parcelize


class CharacterNameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCharacterNameBinding

    @Parcelize
    data class Argument(val name: String) : Parcelable

    private val arg by argument<Argument>()

    private val selectedCharacter by lazy {
        MutableStateFlow(arg.name)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharacterNameBinding.inflate(layoutInflater).also { setContentView(it.root) }

        binding.etName.addNoSpaceFilter().addLengthFilter(5)

        initEvent()
        initButtonClickListeners()

    }

    private fun initEvent() {
        collectFlowWhenStarted(selectedCharacter) {
            if (it == "moon") {
                with(binding) {
                    ivRectangleCharacter.setImageResource(R.drawable.ic_rectangle_moon_name)
                    ivCharacter.setImageResource(R.drawable.character_moon)
                }
            } else {
                with(binding) {
                    ivRectangleCharacter.setImageResource(R.drawable.ic_rectangle_cloud_name)
                    ivCharacter.setImageResource(R.drawable.character_cloud)
                }
            }
        }

        binding.etName.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.etName.text.toString().isBlank()){
                    with(binding){
                        tvDone.setTextColor(getColor(R.color.gray7))
                    }
                }else{
                    binding.tvDone.setTextColor(getColor(R.color.orange))
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun initButtonClickListeners() {
        showToast("5글자 이내로 입력해주세요")
        binding.ibBack.setOnClickListener {
            finish()
        }

        binding.tvDone.setOnClickListener {
            val characterName = binding.etName.text.toString()
            if(characterName.isNotBlank()){
                binding.textView.text = "당신의 $characterName 이(가) 오고 있어요\n잠시 기다려주세요"

                val textView = binding.textView.text.toString()
                val builder = SpannableStringBuilder(textView)
                val colorSpan = ForegroundColorSpan(getColor(R.color.orange))
                builder.setSpan(colorSpan,4,4 + characterName.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                binding.textView.text = builder

                with(binding){
                    ibBack.isInvisible = true
                    tvDone.isInvisible = true
                    etName.isInvisible = true
                    progressCir.isVisible = true
                }
            }
        }
    }
}

