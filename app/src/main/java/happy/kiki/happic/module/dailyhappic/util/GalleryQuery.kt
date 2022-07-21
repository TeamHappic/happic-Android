package happy.kiki.happic.module.dailyhappic.util

import android.provider.MediaStore

object GalleryQuery {
    fun asd() {
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        )
    }
}
