package happy.kiki.happic.module.auth.ui.activity

import android.os.Bundle
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.buildSpannedString
import androidx.core.text.underline
import androidx.lifecycle.lifecycleScope
import com.kakao.sdk.common.model.ClientError
import happy.kiki.happic.R
import happy.kiki.happic.databinding.ActivitySignInBinding
import happy.kiki.happic.module.auth.provider.AuthProvider
import happy.kiki.happic.module.core.util.debugE
import happy.kiki.happic.module.core.util.extension.showToast
import happy.kiki.happic.module.core.util.extension.windowHandler

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivitySignInBinding.inflate(layoutInflater).also {
            binding = it;setContentView(it.root)
        }

        windowHandler.run {
            setStatusBarTransparent()
            allowViewOverlapWithStatusBar()
        }

        // FIXME clickable implementation
        binding.bottomText.isClickable = true
        binding.bottomText.movementMethod = LinkMovementMethod.getInstance()
        binding.bottomText.text = buildSpannedString {
            append("로그인 시 ")

            underline {
                append("이용약관", object : ClickableSpan() {
                    override fun onClick(p0: View) {
                        debugE("123")
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.isUnderlineText = false
                        ds.color = getColor(R.color.gray6)
                    }
                }, 0)
            }
            append("과 ")
            underline {
                append("개인정보 처리 방침")
            }
            append("에 동의하게 됩니다.")
        }

        configureKaKaoLogin()
    }

    private fun configureKaKaoLogin() {
        binding.kakaoButton.setOnClickListener {
            lifecycleScope.launchWhenStarted {
                kotlin.runCatching {
                    AuthProvider.signOut()
                    AuthProvider.signInWithKakao(this@SignInActivity)
                }.onSuccess { token -> // TODO sign in
                }.onFailure {
                    val isCancelled = it is ClientError && it.msg.contains("cancel")
                    if (!isCancelled) showToast("카카오 로그인 실패")
                }
            }
        }
    }
}