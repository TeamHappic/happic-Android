package happy.kiki.happic.module.upload.ui.activity

import androidx.lifecycle.ViewModel
import happy.kiki.happic.module.core.data.api.base.useApiNoParams
import happy.kiki.happic.module.dailyhappic.data.api.DailyHappicService.KeywordRankingForUploadRes
import happy.kiki.happic.module.dailyhappic.data.api.dailyHappicMockService
import happy.kiki.happic.module.upload.data.model.UploadFieldType.WHEN
import happy.kiki.happic.module.upload.data.model.UploadFieldType.WHO
import happy.kiki.happic.module.upload.data.model.UploadFieldType.WHERE
import happy.kiki.happic.module.upload.data.model.UploadFieldType.WHAT
import kotlinx.coroutines.flow.MutableStateFlow

class UploadHappicViewModel : ViewModel() {
    val isUploadFieldFocused = MutableStateFlow(false)
    val isUploadBtnEnabled = MutableStateFlow(false)

    val inputs = mapOf(
        WHEN to MutableStateFlow(false),
        WHERE to MutableStateFlow(false),
        WHO to MutableStateFlow(false),
        WHAT to MutableStateFlow(false)
    )

    val dailyHappicKeywordApi = useApiNoParams<KeywordRankingForUploadRes> {
        dailyHappicMockService.keywordRankingForUpload()
    }

    init {
        dailyHappicKeywordApi.call()
    }
}