package happy.kiki.happic.module.main.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import happy.kiki.happic.databinding.ActivityCharacterBinding

class CharacterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCharacterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharacterBinding.inflate(layoutInflater).also { setContentView(it.root) }
    }
}