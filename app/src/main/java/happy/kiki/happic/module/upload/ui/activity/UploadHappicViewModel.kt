package happy.kiki.happic.module.upload.ui.activity

import androidx.lifecycle.ViewModel
import happy.kiki.happic.module.core.data.api.base.useApi
import happy.kiki.happic.module.core.data.api.base.useApiNoParams
import happy.kiki.happic.module.todayhappic.data.api.TodayHappicService.KeywordRankingForUploadRes
import kotlinx.coroutines.flow.MutableStateFlow

class UploadHappicViewModel : ViewModel() {
    val isUploadFieldFocused = MutableStateFlow(false)

//    val happicKeywordApi = useApiNoParams<KeywordRankingForUploadRes>(onSuccess = {}, onError = {}) {}
}