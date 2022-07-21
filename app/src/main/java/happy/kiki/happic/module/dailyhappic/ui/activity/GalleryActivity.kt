package happy.kiki.happic.module.dailyhappic.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import happy.kiki.happic.databinding.ActivityGalleryBinding

class GalleryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGalleryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityGalleryBinding.inflate(layoutInflater).also { binding = it;setContentView(it.root) }

        configureHeader()
    }

    private fun configureHeader() {
        binding.back.setOnClickListener { finish() }
    }
}