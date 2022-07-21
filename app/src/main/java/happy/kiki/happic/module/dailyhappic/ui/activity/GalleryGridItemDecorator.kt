package happy.kiki.happic.module.dailyhappic.ui.activity

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GalleryGridItemDecorator(private val space: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val pos = parent.getChildLayoutPosition(view)
        outRect.right = if (pos % 3 == 0) 0 else space
        outRect.bottom = space
    }
}