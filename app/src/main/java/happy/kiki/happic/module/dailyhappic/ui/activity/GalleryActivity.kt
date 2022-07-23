package happy.kiki.happic.module.dailyhappic.ui.activity

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import happy.kiki.happic.databinding.ActivityGalleryBinding
import happy.kiki.happic.module.core.util.extension.collectFlowWhenStarted
import happy.kiki.happic.module.core.util.extension.px
import happy.kiki.happic.module.core.util.now
import happy.kiki.happic.module.dailyhappic.data.model.GalleryModel
import happy.kiki.happic.module.dailyhappic.util.GalleryQuery
import java.time.ZoneId

class GalleryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGalleryBinding
    private val vm by viewModels<GalleryViewModel>()

    private val newImageUri by lazy {
        val nowUnixMillis = now.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000
        contentResolver.insert(Media.EXTERNAL_CONTENT_URI, ContentValues().apply {
            val title = "Happic-$nowUnixMillis.jpg"
            put(Media.TITLE, title)
            put(Media.DISPLAY_NAME, title)
            put(Media.MIME_TYPE, "image/jpeg")
            put(Media.DATE_ADDED, nowUnixMillis)
            put(Media.DATE_TAKEN, nowUnixMillis)
        })
    }

    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
        if (success) {
            contentResolver.query(newImageUri!!, null, null, null, null)?.run {
                moveToNext()
                val idx = getColumnIndex(MediaStore.MediaColumns.DATA)
                onSelectItem(this.getString(idx))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityGalleryBinding.inflate(layoutInflater).also { binding = it;setContentView(it.root) }

        vm.imagePaths.value = GalleryQuery(this).listAllImagePaths()
        configureHeader()
        configureList()
    }

    private fun configureHeader() = binding.back.setOnClickListener { finish() }

    private fun configureList() {
        val adapter = GalleryAdapter({
            onTakePicture()
        }, {
            onSelectItem(it)
        })
        binding.list.run {
            this.adapter = adapter
            addItemDecoration(GalleryGridItemDecorator(px(4)))
        }
        collectFlowWhenStarted(vm.imagePaths) {
            adapter.submitList((listOf("dummy") + it).map { GalleryModel(it) })
        }
    }

    private fun onTakePicture() {
        takePicture.launch(newImageUri)
    }

    private fun onSelectItem(uri: String) {
        setResult(100, Intent().apply {
            putExtra("uri", uri)
        })
        finish()
    }
}