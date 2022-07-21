package happy.kiki.happic.module.upload.ui.activity

import androidx.lifecycle.ViewModel
import happy.kiki.happic.module.core.data.api.base.useApiNoParams
import happy.kiki.happic.module.dailyhappic.data.api.DailyHappicService.KeywordRankingForUploadRes
import happy.kiki.happic.module.dailyhappic.data.api.dailyHappicMockService
import kotlinx.coroutines.flow.MutableStateFlow

class UploadHappicViewModel : ViewModel() {
    val isUploadFieldFocused = MutableStateFlow(false)
    val isUploadBtnEnabled = MutableStateFlow(false)

    val inputs = mapOf(
        "#when" to MutableStateFlow(false),
        "#where" to MutableStateFlow(false),
        "#who" to MutableStateFlow(false),
        "#what" to MutableStateFlow(false)
    )

    val dailyHappicKeywordApi = useApiNoParams<KeywordRankingForUploadRes> {
        dailyHappicMockService.keywordRankingForUpload()
    }

    init {
        dailyHappicKeywordApi.call()
    }
}