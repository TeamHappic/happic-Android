package happy.kiki.happic.module.upload.ui.activity

import androidx.lifecycle.ViewModel
import happy.kiki.happic.module.core.data.api.base.useApiNoParams
import happy.kiki.happic.module.dailyhappic.data.api.DailyHappicService.KeywordRankingForUploadRes
import happy.kiki.happic.module.dailyhappic.data.api.dailyHappicMockService
import kotlinx.coroutines.flow.MutableStateFlow

class UploadHappicViewModel : ViewModel() {
    val isUploadFieldFocused = MutableStateFlow(false)

    val dailyHappicKeywordApi = useApiNoParams<KeywordRankingForUploadRes> {
        dailyHappicMockService.keywordRankingForUpload()
    }

    init {
        dailyHappicKeywordApi.call()
    }
}