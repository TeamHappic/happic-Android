package happy.kiki.happic.module.upload.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import happy.kiki.happic.databinding.ActivityUploadHappicBinding

class UploadHappicActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadHappicBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityUploadHappicBinding.inflate(layoutInflater).also { binding = it;setContentView(it.root) }

    }
}