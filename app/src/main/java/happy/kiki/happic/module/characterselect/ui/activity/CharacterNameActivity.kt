package happy.kiki.happic.module.characterselect.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import happy.kiki.happic.R
import happy.kiki.happic.databinding.ActivityCharacterNameBinding
import happy.kiki.happic.module.auth.data.api.AuthService.SignUpReq
import happy.kiki.happic.module.characterselect.data.api.CharacterService.UpdateCharacterReq
import happy.kiki.happic.module.characterselect.provider.CharacterSelectFlowProvider
import happy.kiki.happic.module.characterselect.provider.CharacterSelectFlowProvider.Usage.SIGNUP
import happy.kiki.happic.module.core.util.extension.addLengthFilter
import happy.kiki.happic.module.core.util.extension.addNoSpaceFilter
import happy.kiki.happic.module.core.util.extension.collectFlowWhenStarted
import happy.kiki.happic.module.core.util.extension.pushActivity
import happy.kiki.happic.module.core.util.extension.showToast
import happy.kiki.happic.module.core.util.extension.windowHandler
import happy.kiki.happic.module.main.ui.activity.MainActivity

class CharacterNameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCharacterNameBinding

    private val vm by viewModels<CharacterNameViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharacterNameBinding.inflate(layoutInflater).also { setContentView(it.root) }

        binding.etName.addNoSpaceFilter().addLengthFilter(5)

        initEvent()
        initButtonClickListeners()
        bindSignUpApiState()
        bindUpdateApiState()
    }

    private fun initEvent() {
        collectFlowWhenStarted(CharacterSelectFlowProvider.character) {
            binding.ivCharacter.setImageResource(it.stateByLevel(1).drawableRes)
        }

        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.etName.text.toString().isBlank()) {
                    with(binding) {
                        tvDone.setTextColor(getColor(R.color.gray7))
                    }
                } else {
                    binding.tvDone.setTextColor(getColor(R.color.orange))
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun initButtonClickListeners() {
        showToast("5?????? ????????? ??????????????????")
        binding.ibBack.setOnClickListener {
            finish()
        }

        binding.tvDone.setOnClickListener {
            val characterName = binding.etName.text.toString()
            if (characterName.isNotBlank()) {
                CharacterSelectFlowProvider.name.value = characterName

                if (CharacterSelectFlowProvider.usage == SIGNUP) {
                    vm.signUpAndSignInApi.call(
                        SignUpReq(
                            "kakao",
                            CharacterSelectFlowProvider.character.value,
                            CharacterSelectFlowProvider.name.value,
                            CharacterSelectFlowProvider.snsAccessToken
                        )
                    )
                } else {
                    vm.updateCharacter.call(
                        UpdateCharacterReq(
                            CharacterSelectFlowProvider.character.value,
                            CharacterSelectFlowProvider.name.value,
                        )
                    )
                }
            }
        }
    }

    private fun bindSignUpApiState() {
        collectFlowWhenStarted(vm.signUpAndSignInApi.isLoading, ::bindWithLoadingState)
        collectFlowWhenStarted(vm.signUpAndSignInApi.isSuccess) {
            if (it) pushActivity<MainActivity>(intentConfig = {
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            })
        }
    }

    private fun bindUpdateApiState() {
        collectFlowWhenStarted(vm.updateCharacter.isLoading, ::bindWithLoadingState)

        collectFlowWhenStarted(vm.updateCharacter.isSuccess) {
            if (it) pushActivity<MainActivity>(intentConfig = {
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            })
        }
    }

    private fun bindWithLoadingState(isLoading: Boolean) {
        val characterName = binding.etName.text.toString()
        if (isLoading) {
            binding.textView.text = "????????? $characterName ???(???) ?????? ?????????\n?????? ??????????????????"
            val textView = binding.textView.text.toString()
            val builder = SpannableStringBuilder(textView)
            val colorSpan = ForegroundColorSpan(getColor(R.color.orange))
            builder.setSpan(colorSpan, 4, 4 + characterName.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.textView.text = builder
        } else {
            binding.textView.text = "?????? ???????????? ?????? ????????????"
        }

        binding.ibBack.isInvisible = isLoading
        binding.tvDone.isInvisible = isLoading
        binding.etName.isInvisible = isLoading
        binding.progressCir.isVisible = isLoading

        if (isLoading) windowHandler.hideKeyboard()
    }
}

