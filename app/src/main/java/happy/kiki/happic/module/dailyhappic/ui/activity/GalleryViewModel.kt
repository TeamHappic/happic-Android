package happy.kiki.happic.module.dailyhappic.ui.activity

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class GalleryViewModel: ViewModel() {
    val imagePaths = MutableStateFlow(listOf<String>())
    init {

    }
}