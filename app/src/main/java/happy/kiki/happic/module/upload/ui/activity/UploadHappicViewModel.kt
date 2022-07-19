package happy.kiki.happic.module.upload.ui.activity

import androidx.lifecycle.ViewModel
import happy.kiki.happic.module.core.data.api.base.useApi
import happy.kiki.happic.module.core.data.api.base.useApiNoParams
import happy.kiki.happic.module.core.util.extension.collectFlow
import happy.kiki.happic.module.report.data.api.reportMockService
import happy.kiki.happic.module.report.data.enumerate.ReportCategoryOption
import happy.kiki.happic.module.report.data.model.ReportHomeModel
import happy.kiki.happic.module.todayhappic.data.api.TodayHappicService.KeywordRankingForUploadRes
import happy.kiki.happic.module.todayhappic.data.api.todayHappicKeywordMockService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class UploadHappicViewModel : ViewModel() {
    val isUploadFieldFocused = MutableStateFlow(false)

    val todayHappicKeywordApi = useApiNoParams<KeywordRankingForUploadRes> {
        todayHappicKeywordMockService.keywordRankingForUpload()
    }

    init {
        todayHappicKeywordApi.call()
    }
}