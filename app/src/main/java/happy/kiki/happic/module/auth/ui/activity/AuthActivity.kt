package happy.kiki.happic.module.auth.ui.activity

import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import happy.kiki.happic.databinding.ActivityAuthBinding
import happy.kiki.happic.module.core.util.extension.replaceActivity
import happy.kiki.happic.module.main.ui.activity.MainActivity

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        ActivityAuthBinding.inflate(layoutInflater).also { binding = it;setContentView(it.root) }

        postponeSplashScreenExitUntilAutoSignInChecked()
    }

    private fun postponeSplashScreenExitUntilAutoSignInChecked() = binding.root.run {
        viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                return if (true) { // FIXME check auto sign in
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
        replaceActivity<MainActivity>()
        overridePendingTransition(0, 0)
    }
}