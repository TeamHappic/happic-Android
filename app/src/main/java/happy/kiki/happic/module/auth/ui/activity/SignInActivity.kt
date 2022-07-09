package happy.kiki.happic.module.auth.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import happy.kiki.happic.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivitySignInBinding.inflate(layoutInflater).also { binding = it;setContentView(it.root) }

    }
}