package happy.kiki.happic.module.dailyhappic.util

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore

class GalleryQuery(private val context: Context) {
    fun listAllImagePaths(): List<String> {
        val ret = mutableListOf<String>()
        runWithCursor {
            while (moveToNext()) ret.add(getImageUri())
        }
        return ret
    }

    private inline fun runWithCursor(crossinline block: Cursor.() -> Unit) {
        val projection = arrayOf(
            MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATA
        )
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        context.contentResolver.query(uri, projection, null, null, MediaStore.Images.Media.DATE_TAKEN + " DESC")?.run {
            block(this)
            close()
        }
    }

    private fun Cursor.getImageUri(): String {
        val dataIndex = getColumnIndex(MediaStore.Images.Media.DATA)
        return getString(dataIndex)
    }
}
