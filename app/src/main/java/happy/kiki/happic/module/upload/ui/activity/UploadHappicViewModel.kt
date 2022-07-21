package happy.kiki.happic.module.upload.ui.activity

import androidx.lifecycle.ViewModel
import happy.kiki.happic.module.core.data.api.CoreService.UploadPhotoRes
import happy.kiki.happic.module.core.data.api.base.useApi
import happy.kiki.happic.module.core.data.api.base.useApiNoParams
import happy.kiki.happic.module.core.data.api.coreMockService
import happy.kiki.happic.module.core.util.EventFlow
import happy.kiki.happic.module.dailyhappic.data.api.DailyHappicService.DailyHappicUploadReq
import happy.kiki.happic.module.dailyhappic.data.api.DailyHappicService.DailyHappicUploadRes
import happy.kiki.happic.module.dailyhappic.data.api.dailyHappicMockService
import happy.kiki.happic.module.upload.data.model.UploadFieldType.WHAT
import happy.kiki.happic.module.upload.data.model.UploadFieldType.WHEN
import happy.kiki.happic.module.upload.data.model.UploadFieldType.WHERE
import happy.kiki.happic.module.upload.data.model.UploadFieldType.WHO
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.MultipartBody

class UploadHappicViewModel : ViewModel() {
    val isUploadFieldFocused = MutableStateFlow(false)
    val isUploadBtnEnabled = MutableStateFlow(false)

    val isNotEmptyInputs = mapOf(
        WHEN to MutableStateFlow(false),
        WHERE to MutableStateFlow(false),
        WHO to MutableStateFlow(false),
        WHAT to MutableStateFlow(false)
    )

    val inputs = mapOf(
        WHEN to MutableStateFlow(""),
        WHERE to MutableStateFlow(""),
        WHO to MutableStateFlow(""),
        WHAT to MutableStateFlow("")
    )

    val onImageUpload = EventFlow<String>()

    val keywordApi = useApiNoParams {
        dailyHappicMockService.keywordRankingForUpload()
    }

    val uploadApi = useApi<DailyHappicUploadReq, DailyHappicUploadRes> {
        dailyHappicMockService.upload(it)
    }

    val uploadPhotoApi = useApi<MultipartBody.Part, UploadPhotoRes>(onSuccess = {
        onImageUpload.emit(it.link)
    }) { //        coreService.uploadPhoto(it)
        coreMockService.uploadPhoto(it)
    }

    init {
        keywordApi.call()
    }
}