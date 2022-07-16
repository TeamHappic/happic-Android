package happy.kiki.happic.module.upload.ui.activity

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class UploadHappicViewModel : ViewModel() {
    val isUploadFieldFocused = MutableStateFlow(false)
}