package happy.kiki.happic.module.characterselect.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import happy.kiki.happic.databinding.ActivityCharacterNameBinding

class CharacterNameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCharacterNameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharacterNameBinding.inflate(layoutInflater).also { setContentView(it.root) }
    }


}

