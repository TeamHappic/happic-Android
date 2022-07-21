package happy.kiki.happic.module.auth.ui.activity

import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import happy.kiki.happic.databinding.ActivityAuthBinding
import happy.kiki.happic.module.auth.data.enumerate.AutoSignInResult
import happy.kiki.happic.module.core.util.extension.pushActivity
import happy.kiki.happic.module.core.util.extension.windowHandler
import happy.kiki.happic.module.main.ui.activity.MainActivity

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        ActivityAuthBinding.inflate(layoutInflater).also { binding = it;setContentView(it.root) }

        postponeSplashScreenExitUntilAutoSignInChecked()

        windowHandler.allowViewOverlapWithStatusBar()
        windowHandler.hideStatusBar()
    }

    private fun postponeSplashScreenExitUntilAutoSignInChecked() = binding.root.run {
        viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                return if (viewModel.autoSignInResult.value != AutoSignInResult.PENDING) {
                    viewTreeObserver.removeOnPreDrawListener(this)
                    navigateNextActivity()
                    true
                } else {
                    false
                }
            }
        })
    }

    private fun navigateNextActivity() {
        if (viewModel.autoSignInResult.value == AutoSignInResult.SUCCESS) pushActivity<MainActivity>()
        else pushActivity<SignInActivity>()
        overridePendingTransition(0, 0)
    }
}