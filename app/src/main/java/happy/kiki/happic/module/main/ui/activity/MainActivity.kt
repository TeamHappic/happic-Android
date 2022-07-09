package happy.kiki.happic.module.main.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import happy.kiki.happic.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
    }
}