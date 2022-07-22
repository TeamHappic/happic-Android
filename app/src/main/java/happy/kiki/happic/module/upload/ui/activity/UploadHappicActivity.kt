package happy.kiki.happic.module.upload.ui.activity

import android.animation.ValueAnimator
import android.graphics.drawable.ShapeDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.ViewGroup.MarginLayoutParams
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.core.view.ViewCompat
import androidx.core.view.updateLayoutParams
import com.google.android.material.chip.Chip
import happy.kiki.happic.R
import happy.kiki.happic.databinding.ActivityUploadHappicBinding
import happy.kiki.happic.databinding.ItemUploadChipBinding
import happy.kiki.happic.module.core.util.extension.argument
import happy.kiki.happic.module.core.util.extension.collectFlowWhenStarted
import happy.kiki.happic.module.core.util.extension.px
import happy.kiki.happic.module.core.util.extension.pxFloat
import happy.kiki.happic.module.core.util.extension.screenHeight
import happy.kiki.happic.module.core.util.extension.screenWidth
import happy.kiki.happic.module.core.util.extension.windowHandler
import happy.kiki.happic.module.core.util.now
import happy.kiki.happic.module.report.data.enumerate.ReportCategoryOption.hour
import happy.kiki.happic.module.report.data.enumerate.ReportCategoryOption.what
import happy.kiki.happic.module.report.data.enumerate.ReportCategoryOption.where
import happy.kiki.happic.module.report.data.enumerate.ReportCategoryOption.who
import happy.kiki.happic.module.report.util.koFormat
import happy.kiki.happic.module.report.util.monthDateFormat
import happy.kiki.happic.module.report.util.nowDate
import kotlinx.android.parcel.Parcelize

class UploadHappicActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadHappicBinding
    private val vm by viewModels<UploadHappicViewModel>()

    @Parcelize
    data class Argument(val uri: String) : Parcelable

    private val arg by argument<Argument>()

    private val expandedImageSize get() = px(260).coerceAtMost(screenWidth - px(50) * 2)
    private val collapsedImageSize
        get() = screenHeight - px(60) - px(16) - px(20) - px(48) * 4 - px(8) * 3 - px(100) - px(
            300
        )

    private val animationDuration = 250L
    private var photoSizeAnimator: ValueAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm.photoUri.value = arg.uri
        ActivityUploadHappicBinding.inflate(layoutInflater).also { binding = it;setContentView(it.root) }
        configureHeader()
        bindingDatas()

        setInitialUiToInputs()
        binding.containerFields.setOnClickListener {
            windowHandler.hideKeyboard()
            vm.focusedInput.value = null
            binding.whenInput.textField.clearFocus()
            binding.whereInput.textField.clearFocus()
            binding.whoInput.textField.clearFocus()
            binding.whatInput.textField.clearFocus()
        }
        configurePhoto()
        configureWhenInput()
        configureWhereInput()
        configureWhoInput()
        configureWhatInput()
    }

    private fun configureHeader() {
        collectFlowWhenStarted(vm.isUploadButtonEnabled) { isEnable ->
            binding.tvUpload.setTextColor(getColor(if (isEnable) R.color.orange else R.color.gray7))
            binding.tvUpload.isClickable = isEnable
        }
        binding.title.text = buildSpannedString {
            bold {
                append(nowDate.monthDateFormat)
            }
            append(" 해픽")
        }
        binding.close.setOnClickListener {
            finish()
        }
        binding.tvUpload.setOnClickListener {
            vm.upload()
        }
    }

    private fun bindingDatas() {
        collectFlowWhenStarted(vm.photoUri) {
            binding.ivPhoto.setImageURI(Uri.parse(it))
        }
        collectFlowWhenStarted(vm.hour) {

        }
    }

    private fun setInitialUiToInputs() {
        listOf(
            "#when" to "시간을 입력해주세요" to binding.whenInput,
            "#where" to "장소를 입력해주세요" to binding.whereInput,
            "#who" to "함께한 사람을 입력해주세요" to binding.whoInput,
            "#what" to "무엇을 했는지 입력해주세요" to binding.whatInput,
        ).forEach { (data, input) ->
            input.category.text = data.first
            input.textField.hint = data.second
        }
    }

    private fun configurePhoto() {
        binding.ivPhoto.updateLayoutParams {
            width = expandedImageSize
            height = expandedImageSize
        }

        collectFlowWhenStarted(vm.isPhotoExpanded) {
            photoSizeAnimator?.cancel()
            photoSizeAnimator =
                ValueAnimator.ofInt(binding.ivPhoto.width, if (it) expandedImageSize else collapsedImageSize).apply {
                    duration = animationDuration
                    addUpdateListener {
                        val value = it.animatedValue as Int
                        binding.ivPhoto.updateLayoutParams {
                            width = value
                            height = value
                        }
                    }
                    start()
                }
        }
    }

    private fun configureWhenInput() {
        binding.whenInput.run {
            textField.isFocusable = false
            textField.isClickable = false
        }
        binding.whenInputClick.setOnClickListener {
            windowHandler.hideKeyboard()
            if (vm.focusedInput.value == hour) vm.focusedInput.value = null
            else vm.focusedInput.value = hour
        }

        collectFlowWhenStarted(vm.focusedInput) {
            binding.containerPicker.animate().translationY(if (it == hour) 0f else pxFloat(300)).alpha(
                if (it == hour) 1f else 0.2f
            ).apply {
                duration = animationDuration
            }.start()
        }
        collectFlowWhenStarted(vm.hour) {
            if (it == -1) binding.timePicker.hour = now.hour
            else binding.timePicker.hour = it

            if (it != -1) binding.whenInput.textField.setText(it.koFormat)
        }
        binding.timePicker.onHourChangedListener = {
            vm.hour.value = it
        }
    }

    private fun configureWhereInput() {
        binding.whereInputClick.setOnClickListener {
            if (vm.focusedInput.value == where) {
                vm.focusedInput.value = null
                windowHandler.hideKeyboard()
                binding.whereInput.textField.clearFocus()
            } else {
                vm.focusedInput.value = where
                windowHandler.showKeyboard()
                binding.whereInput.textField.requestFocus()
            }
        }
    }

    private fun configureWhoInput() {
        binding.whoInputClick.setOnClickListener {
            if (vm.focusedInput.value == who) {
                vm.focusedInput.value = null
                windowHandler.hideKeyboard()
                binding.whoInput.textField.clearFocus()
            } else {
                vm.focusedInput.value = who
                windowHandler.showKeyboard()
                binding.whoInput.textField.requestFocus()
            }
        }
    }

    private fun configureWhatInput() {
        binding.whatInputClick.setOnClickListener {
            if (vm.focusedInput.value == what) {
                vm.focusedInput.value = null
                windowHandler.hideKeyboard()
                binding.whatInput.textField.clearFocus()
            } else {
                vm.focusedInput.value = what
                windowHandler.showKeyboard()
                binding.whatInput.textField.requestFocus()
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