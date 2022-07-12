package happy.kiki.happic.module.characterselect.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import happy.kiki.happic.databinding.ActivityCharacterBinding

class CharacterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCharacterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharacterBinding.inflate(layoutInflater).also { setContentView(it.root) }
    }
}