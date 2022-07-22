package happy.kiki.happic.module.upload.ui.activity

import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils.TruncateAt.END
import android.view.View.TEXT_ALIGNMENT_CENTER
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.widget.addTextChangedListener
import com.google.android.material.chip.Chip
import happy.kiki.happic.R
import happy.kiki.happic.databinding.ActivityUploadHappicBinding
import happy.kiki.happic.module.core.data.api.base.NetworkState.Failure
import happy.kiki.happic.module.core.data.api.base.NetworkState.Success
import happy.kiki.happic.module.core.util.extension.addLengthFilter
import happy.kiki.happic.module.core.util.extension.addNoSpaceFilter
import happy.kiki.happic.module.core.util.extension.argument
import happy.kiki.happic.module.core.util.extension.collectFlowWhenStarted
import happy.kiki.happic.module.core.util.extension.injectViewId
import happy.kiki.happic.module.core.util.extension.px
import happy.kiki.happic.module.core.util.extension.pxFloat
import happy.kiki.happic.module.core.util.extension.screenHeight
import happy.kiki.happic.module.core.util.extension.screenWidth
import happy.kiki.happic.module.core.util.extension.showToast
import happy.kiki.happic.module.core.util.extension.windowHandler
import happy.kiki.happic.module.core.util.now
import happy.kiki.happic.module.report.data.enumerate.ReportCategoryOption.what
import happy.kiki.happic.module.report.data.enumerate.ReportCategoryOption.whenn
import happy.kiki.happic.module.report.data.enumerate.ReportCategoryOption.where
import happy.kiki.happic.module.report.data.enumerate.ReportCategoryOption.who
import happy.kiki.happic.module.report.util.koFormat
import happy.kiki.happic.module.report.util.monthDateFormat
import happy.kiki.happic.module.report.util.nowDate
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.flow.combine
import java.lang.Math.max

class UploadHappicActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadHappicBinding
    private val vm by viewModels<UploadHappicViewModel>()

    @Parcelize
    data class Argument(val uri: String) : Parcelable

    private val arg by argument<Argument>()

    private val expandedImageSize get() = px(260).coerceAtMost(screenWidth - px(50) * 2)
    private val collapsedImageSize
        get() = max(
            px(140), screenHeight - px(60) - px(16) - px(20) - px(48) * 4 - px(8) * 3 - px(100) - px(
                300
            )
        )

    private val animationDuration = 250L
    private var photoSizeAnimator: ValueAnimator? = null

    private val chipContainerAnimators = hashMapOf<Int, ValueAnimator?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityUploadHappicBinding.inflate(layoutInflater).also { binding = it;setContentView(it.root) }
        vm.photoUri.value = arg.uri

        binding.lifecycleOwner = this
        binding.vm = vm

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
        configureChipContainerAnimation()
        collectApiResult()
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
            input.textField.addNoSpaceFilter().addLengthFilter(5)
        }

        binding.whenInput.chipContainer.isVisible = false
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

    private fun openWhen() {
        windowHandler.hideKeyboard()
        vm.focusedInput.value = whenn
    }

    private fun closeWhen() {
        windowHandler.hideKeyboard()
        vm.focusedInput.value = null
    }

    private fun openWhere() {
        vm.focusedInput.value = where
        windowHandler.showKeyboard()
        binding.whereInput.textField.requestFocus()
    }

    private fun closeWhere() {
        vm.focusedInput.value = null
        windowHandler.hideKeyboard()
        binding.whereInput.textField.clearFocus()
    }

    private fun openWho() {
        vm.focusedInput.value = who
        windowHandler.showKeyboard()
        binding.whoInput.textField.requestFocus()
    }

    private fun closeWho() {
        vm.focusedInput.value = null
        windowHandler.hideKeyboard()
        binding.whoInput.textField.clearFocus()
    }

    private fun openWhat() {
        vm.focusedInput.value = what
        windowHandler.showKeyboard()
        binding.whatInput.textField.requestFocus()
    }

    private fun closeWhat() {
        vm.focusedInput.value = null
        windowHandler.hideKeyboard()
        binding.whatInput.textField.clearFocus()
    }

    private fun configureWhenInput() {
        binding.whenInput.run {
            textField.isFocusable = false
            textField.isClickable = false
        }
        binding.whenInputClick.setOnClickListener {
            if (vm.focusedInput.value == whenn) closeWhen() else openWhen()
        }

        collectFlowWhenStarted(vm.focusedInput) {
            binding.containerPicker.animate().translationY(if (it == whenn) 0f else pxFloat(300)).alpha(
                if (it == whenn) 1f else 0.2f
            ).apply {
                duration = animationDuration
            }.start()

            binding.whenInput.root.strokeColor = getColor(if (it == whenn) R.color.dark_blue else R.color.transparent)
        }
        collectFlowWhenStarted(vm.hour) {
            if (it == -1) binding.timePicker.hour = now.hour
            else binding.timePicker.hour = it

            if (it != -1) binding.whenInput.textField.setText(it.koFormat)
        }
        binding.timePicker.onHourChangedListener = {
            vm.hour.value = it
        }
        binding.btnComplete.setOnClickListener {
            vm.focusedInput.value = null
        }
    }

    private fun generateChip(text: String, onClickListener: (String) -> Unit): Chip {
        return Chip(this).apply {
            injectViewId()
            this.text = text
            setOnClickListener { onClickListener(text) }
            textAlignment = TEXT_ALIGNMENT_CENTER
            setTextAppearance(R.style.C2_P_M12)
            setTextColor(getColor(R.color.gray4))
            setEnsureMinTouchTargetSize(false)
            chipMinHeight = px(36).toFloat()
            chipBackgroundColor = ColorStateList.valueOf(getColor(R.color.gray9))
            layoutParams = ConstraintLayout.LayoutParams(0, px(36)).apply {
                horizontalWeight = 1f
            }
            ellipsize = END
        }
    }

    private fun configureWhereInput() {
        val input = binding.whereInput

        binding.whereInputClick.setOnClickListener {
            if (vm.focusedInput.value == where) closeWhere() else openWhere()
        }

        collectFlowWhenStarted(vm.focusedInput) {
            input.root.strokeColor = getColor(if (it == where) R.color.dark_blue else R.color.transparent)
        }

        input.textField.addTextChangedListener {
            vm.where.value = it.toString()
        }
        input.textField.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) openWhere()
        }

        collectFlowWhenStarted(vm.whereKeywords) {
            binding.whereInput.chipContainer.children.forEach {
                if (it !is Flow) binding.whereInput.chipContainer.removeView(it)
            }
            it.forEach {
                val chip = generateChip(it) {
                    vm.where.value = it
                    binding.whereInput.textField.setText(it)
                    openWho()
                    binding.scrollView.smoothScrollBy(0, px(200))
                }
                binding.whereInput.chipContainer.addView(chip)
                binding.whereInput.chipFlow.addView(chip)
            }
        }

    }

    private fun configureWhoInput() {
        val input = binding.whoInput

        binding.whoInputClick.setOnClickListener {
            if (vm.focusedInput.value == who) closeWho() else openWho()
        }

        collectFlowWhenStarted(vm.focusedInput) {
            input.root.strokeColor = getColor(if (it == who) R.color.dark_blue else R.color.transparent)
        }

        input.textField.addTextChangedListener {
            vm.who.value = it.toString()
        }
        input.textField.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) openWho()
        }

        collectFlowWhenStarted(vm.whoKeywords) {
            input.chipContainer.children.forEach {
                if (it !is Flow) binding.whoInput.chipContainer.removeView(it)
            }
            it.forEach {
                val chip = generateChip(it) {
                    vm.who.value = it
                    input.textField.setText(it)
                    openWhat()
                    binding.scrollView.smoothScrollBy(0, px(200))
                }
                input.chipContainer.addView(chip)
                input.chipFlow.addView(chip)
            }
        }
    }

    private fun configureWhatInput() {
        val input = binding.whatInput

        binding.whatInputClick.setOnClickListener {
            if (vm.focusedInput.value == what) closeWhat() else openWhat()
        }

        collectFlowWhenStarted(vm.focusedInput) {
            input.root.strokeColor = getColor(if (it == what) R.color.dark_blue else R.color.transparent)
        }

        input.textField.addTextChangedListener {
            vm.what.value = it.toString()
        }
        input.textField.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) openWhat()
        }

        collectFlowWhenStarted(vm.whatKeywords) {
            input.chipContainer.children.forEach {
                if (it !is Flow) input.chipContainer.removeView(it)
            }
            it.forEach {
                val chip = generateChip(it) {
                    vm.what.value = it
                    input.textField.setText(it)
                    closeWhat()
                    binding.scrollView.smoothScrollBy(0, px(200))
                }
                input.chipContainer.addView(chip)
                input.chipFlow.addView(chip)
            }
        }
    }

    private fun configureChipContainerAnimation() {
        collectFlowWhenStarted(combine(
            vm.focusedInput, vm.whereKeywords, vm.whoKeywords, vm.whatKeywords
        ) { focusedInput, where, who, what ->
            focusedInput to listOf(where, who, what)
        }) { (focusedInput, list) ->
            listOf(
                binding.whereInput.chipContainer, binding.whoInput.chipContainer, binding.whatInput.chipContainer
            ).forEachIndexed { index, container ->
                chipContainerAnimators[index]?.cancel()
                val shouldShow = list[index].isNotEmpty() && when (focusedInput) {
                    where -> index == 0
                    who -> index == 1
                    what -> index == 2
                    else -> false
                }
                val rowCount = (list[index].size + 3 - 1) / 3
                val targetHeight = px(12) * 2 + rowCount * px(36) + max(0, rowCount - 1) * px(8)
                chipContainerAnimators[index] =
                    ValueAnimator.ofInt(container.height, if (shouldShow) targetHeight else 0).apply {
                        duration = animationDuration
                        addUpdateListener {
                            val value = it.animatedValue as Int
                            container.updateLayoutParams {
                                height = value
                            }
                        }
                        start()
                    }
                container.animate().alpha(if (shouldShow) 1f else 0f).setDuration(animationDuration).start()
            }
        }
    }

    private fun collectApiResult() {
        collectFlowWhenStarted(vm.uploadApi.isLoading) {
            binding.tvUpload.isEnabled = !it

        }
        collectFlowWhenStarted(vm.uploadApi.state) {
            when (it) {
                is Success -> finish()
                is Failure -> showToast("등록 실패")
                else -> {}
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        chipContainerAnimators.forEach { (_, u) ->
            u?.cancel()
        }
    }
}