package happy.kiki.happic.module.upload.ui.activity

import androidx.lifecycle.ViewModel
import happy.kiki.happic.module.core.data.api.base.useApi
import happy.kiki.happic.module.core.data.api.base.useApiNoParams
import happy.kiki.happic.module.core.data.api.coreService
import happy.kiki.happic.module.core.util.extension.asStateFlow
import happy.kiki.happic.module.dailyhappic.data.api.DailyHappicService.DailyHappicUploadReq
import happy.kiki.happic.module.dailyhappic.data.api.DailyHappicService.DailyHappicUploadRes
import happy.kiki.happic.module.dailyhappic.data.api.dailyHappicService
import happy.kiki.happic.module.report.data.enumerate.ReportCategoryOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class UploadHappicViewModel : ViewModel() {
    val photoUri = MutableStateFlow("")
    val hour = MutableStateFlow(-1)
    val where = MutableStateFlow("")
    val who = MutableStateFlow("")
    val what = MutableStateFlow("")

    val focusedInput = MutableStateFlow<ReportCategoryOption?>(null)

    val isPhotoExpanded = asStateFlow(focusedInput.map {
        it == null
    }, true)

    private val keywordApi = useApiNoParams { dailyHappicService.keywordRankingForUpload() }
    val whereKeywords = asStateFlow(keywordApi.data.map { it?.where ?: listOf() }, listOf())
    val whoKeywords = asStateFlow(keywordApi.data.map { it?.who ?: listOf() }, listOf())
    val whatKeywords = asStateFlow(keywordApi.data.map { it?.what ?: listOf() }, listOf())

    data class UploadReq(
        val photoUri: String,
        val hour: Int,
        val where: String,
        val who: String,
        val what: String,
    )

    val uploadApi = useApi<UploadReq, DailyHappicUploadRes> { req ->
        val file = File(req.photoUri)
        val requestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", file.name.trim(), requestBody)
        val photoLink = coreService.uploadPhoto(body).data.link
        dailyHappicService.upload(
            DailyHappicUploadReq(
                photoLink, req.hour, req.where, req.who, req.what
            )
        )
    }

    val isUploadButtonEnabled = asStateFlow(combine(hour, where, who, what, uploadApi.isLoading) { h, w1, w2, w3, isLoading ->
        h != -1 && listOf(w1, w2, w3).all { it.isNotBlank() } && !isLoading
    }, false)

    init {
        keywordApi.call()
    }

    fun upload() {
        uploadApi.call(
            UploadReq(
                photoUri.value,
                hour.value,
                where.value,
                who.value,
                what.value,
            )
        )
    }
}