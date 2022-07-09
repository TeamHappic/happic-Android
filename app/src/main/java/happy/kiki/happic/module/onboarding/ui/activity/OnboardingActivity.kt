package happy.kiki.happic.module.onboarding.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import happy.kiki.happic.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityOnboardingBinding.inflate(layoutInflater).also { binding = it;setContentView(it.root) }

    }
}