package happy.kiki.happic.module.dailyhappic.ui.activity

import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import happy.kiki.happic.databinding.ActivityGalleryBinding
import happy.kiki.happic.module.core.util.extension.collectFlowWhenStarted
import happy.kiki.happic.module.core.util.extension.px
import happy.kiki.happic.module.dailyhappic.data.model.GalleryModel
import happy.kiki.happic.module.dailyhappic.util.GalleryQuery

class GalleryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGalleryBinding
    private val vm by viewModels<GalleryViewModel>()

    val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
        if (success) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityGalleryBinding.inflate(layoutInflater).also { binding = it;setContentView(it.root) }

        vm.imagePaths.value = GalleryQuery(this).listAllImagePaths()
        configureHeader()
        configureList()
    }

    private fun configureHeader() {
        binding.back.setOnClickListener { finish() }
    }

    private fun configureList() {
        val adapter = GalleryAdapter {
            takePicture.launch(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        }
        binding.list.run {
            this.adapter = adapter
            addItemDecoration(GalleryGridItemDecorator(px(4)))
        }
        collectFlowWhenStarted(vm.imagePaths) {
            adapter.submitList((listOf("dummy") + it).map { GalleryModel(it) })
        }
    }
}