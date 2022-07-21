package happy.kiki.happic.module.upload.ui.activity

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup.GONE
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.ViewGroup.MarginLayoutParams
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.updateLayoutParams
import androidx.core.widget.addTextChangedListener
import com.google.android.material.chip.Chip
import happy.kiki.happic.R
import happy.kiki.happic.databinding.ActivityUploadHappicBinding
import happy.kiki.happic.databinding.ItemUploadChipBinding
import happy.kiki.happic.databinding.ItemUploadFieldBinding
import happy.kiki.happic.module.core.data.api.base.NetworkState.Success
import happy.kiki.happic.module.core.util.extension.argument
import happy.kiki.happic.module.core.util.extension.collectFlowWhenStarted
import happy.kiki.happic.module.core.util.extension.px
import happy.kiki.happic.module.dailyhappic.data.api.DailyHappicService.DailyHappicUploadReq
import happy.kiki.happic.module.upload.data.model.UploadFieldType.WHAT
import happy.kiki.happic.module.upload.data.model.UploadFieldType.WHEN
import happy.kiki.happic.module.upload.data.model.UploadFieldType.WHERE
import happy.kiki.happic.module.upload.data.model.UploadFieldType.WHO
import kotlinx.android.parcel.Parcelize
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class UploadHappicActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadHappicBinding
    private val vm by viewModels<UploadHappicViewModel>()

    @Parcelize
    data class Argument(val uri: String) : Parcelable

    private val arg by argument<Argument>()
    private val imageUri get() = Uri.parse(arg.uri)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityUploadHappicBinding.inflate(layoutInflater).also { binding = it;setContentView(it.root) }
        configureUIChangeEvent()
        bindingDatas()
        configureFields()
        configureHeader()
        configureUploadEvent()
    }

    private fun configureUploadEvent() {
        binding.tvUpload.setOnClickListener {
            val file = File(imageUri.toString())
            val requestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", file.name.trim(), requestBody)
            vm.uploadPhotoApi.call(body)
        }

        collectFlowWhenStarted(vm.onImageUpload.flow) {
            with(vm) {
                uploadApi.call(
                    DailyHappicUploadReq(
                        it,
                        inputs[WHEN]?.value.toString(),
                        inputs[WHERE]?.value.toString(),
                        inputs[WHO]?.value.toString(),
                        inputs[WHAT]?.value.toString()
                    )
                )
            }
        }

        collectFlowWhenStarted(vm.uploadApi.state) {
            when (it) {
                is Success -> finish()
            }
        }
    }

    private fun configureUIChangeEvent() {
        binding.whole.apply {
            setOnClickListener {
                isFocusableInTouchMode = true
            }
            setOnFocusChangeListener { _, _ ->
                vm.isUploadFieldFocused.value = false
                hideKeyboard()
            }
        }
        collectFlowWhenStarted(vm.isUploadFieldFocused) {
            updateUi(it)
        }
    }

    private fun bindingDatas() {
        binding.ivPhoto.setImageURI(imageUri)
        collectFlowWhenStarted(vm.keywordApi.data) {
            it?.run {
                binding.date = getDate(it.currentDate)
            }
        }
    }

    private fun configureHeader() {
        collectFlowWhenStarted(vm.isUploadBtnEnabled) { isEnable ->
            binding.tvUpload.setTextColor(getColor(if (isEnable) R.color.orange else R.color.gray7))
            binding.tvUpload.isClickable = isEnable
        }
        vm.isNotEmptyInputs.forEach { flowMapEntry ->
            collectFlowWhenStarted(flowMapEntry.value) {
                var check = true
                vm.isNotEmptyInputs.map { it.value.value }.forEach { check = check && it }
                vm.isUploadBtnEnabled.value = check
            }
        }
        binding.close.setOnClickListener {
            finish()
        }
    }

    private fun configureFields() {
        listOf(
            WHEN to "시간을 입력해주세요", WHERE to "장소를 입력해주세요", WHO to "함께한 사람을 입력해주세요", WHAT to "무엇을 했는지 입력해주세요"
        ).forEach {
            ItemUploadFieldBinding.inflate(layoutInflater).apply {
                root.id = ViewCompat.generateViewId()
                title = it.first.title
                hint = it.second
                val fieldType = it.first
                etContent.addTextChangedListener {
                    vm.isNotEmptyInputs[fieldType]?.value = (it.toString().isNotBlank())
                    vm.inputs[fieldType]?.value = (it.toString())
                }

                etContent.setOnFocusChangeListener { _, hasFocus ->
                    vm.isUploadFieldFocused.value = hasFocus
                    borderField.apply {
                        strokeColor = if (hasFocus) context.getColor(R.color.dark_blue) else Color.TRANSPARENT
                        strokeWidth = if (hasFocus) px(1).toFloat() else 0f
                    }

                    containerTags.visibility = when (fieldType) {
                        WHEN -> GONE
                        else -> if (hasFocus) VISIBLE else GONE
                    }

                    // when) 키보드 숨기고, Picker 보이기
                    if (fieldType == WHEN) {
                        hideKeyboard()
                        binding.containerPicker.visibility = if (hasFocus) VISIBLE else GONE
                    }
                }

                if (fieldType == WHEN) {
                    etContent.inputType = EditText.LAYER_TYPE_NONE
                    binding.btnComplete.setOnClickListener { // TODO: timePicker onHourChangedListener 함수 이용해서 변경
                        etContent.setText("오후1시")
                        binding.containerPicker.visibility = GONE
                    }
                } else {
                    collectFlowWhenStarted(vm.keywordApi.data) {
                        it?.run {
                            llTags.removeAllViews()
                            val tagList = when (fieldType) {
                                WHERE -> where
                                WHO -> who
                                else -> what
                            }
                            if (tagList.isEmpty()) {
                                tvEmpty.visibility = VISIBLE
                            } else {
                                tvEmpty.visibility = GONE
                                var idx = 0
                                var linearLayout: LinearLayout? = null

                                tagList.forEach { tag ->
                                    if (idx++ % 3 == 0) {
                                        linearLayout?.let { llTags.addView(linearLayout) }
                                        linearLayout = createLinearLayout()
                                    }

                                    createChip(tag).apply {
                                        setOnClickListener {
                                            etContent.setText(tag)
                                        }
                                        linearLayout?.addView(this)
                                    }
                                }

                                if (idx % 3 != 0) {
                                    repeat(3 - tagList.size % 3) {
                                        createChip("").apply {
                                            visibility = INVISIBLE
                                            linearLayout?.addView(this)
                                        }
                                    }
                                }
                                linearLayout?.let { llTags.addView(linearLayout) }
                            }
                        }
                    }
                }
                binding.containerFields.addView(root)
            }
        }
    }

    private fun createChip(tag: String): Chip = ItemUploadChipBinding.inflate(layoutInflater).root.apply {
        text = tag
        layoutParams = LinearLayout.LayoutParams(0, WRAP_CONTENT, 1f)
    }

    private fun createLinearLayout(): LinearLayout = LinearLayout(this@UploadHappicActivity).apply {
        id = ViewCompat.generateViewId()
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        orientation = LinearLayout.HORIZONTAL
        dividerDrawable = getDivider()
        showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        setPadding(0, px(4), 0, px(4))
    }

    private fun getDivider(): ShapeDrawable = ShapeDrawable().apply {
        intrinsicWidth = px(6)
        alpha = 0
    }

    private fun getDate(date: String): String? {
        date.split("-", " ").apply {
            if (size > 3) {
                return "${this[1]}.${this[2]}"
            }
            return null
        }
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.whole.windowToken, 0)
    }

    private fun updateUi(hasFocus: Boolean) {
        val photoMarginHorizontal = if (hasFocus) 140 else 50
        val containerMarginTop = if (hasFocus) 32 else 20
        binding.ivPhoto.updateLayoutParams<MarginLayoutParams> {
            leftMargin = px(photoMarginHorizontal)
            rightMargin = px(photoMarginHorizontal)
        }
        binding.containerFields.updateLayoutParams<MarginLayoutParams> {
            topMargin = px(containerMarginTop)
        }
    }
}