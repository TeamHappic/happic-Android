package happy.kiki.happic.module.upload.ui.activity

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View.VISIBLE
import android.view.ViewGroup.GONE
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.ViewGroup.MarginLayoutParams
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.updateLayoutParams
import com.google.android.material.chip.Chip
import happy.kiki.happic.R
import happy.kiki.happic.databinding.ActivityUploadHappicBinding
import happy.kiki.happic.databinding.ItemUploadChipBinding
import happy.kiki.happic.databinding.ItemUploadFieldBinding
import happy.kiki.happic.module.core.util.extension.collectFlowWhenStarted
import happy.kiki.happic.module.core.util.extension.px

class UploadHappicActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadHappicBinding
    private val viewModel by viewModels<UploadHappicViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityUploadHappicBinding.inflate(layoutInflater).also { binding = it;setContentView(it.root) }
        setTouchEvent()
        setUpFields()
        setUiEvent()
    }

    private fun setUiEvent() {
        collectFlowWhenStarted(viewModel.isUploadFieldFocused) {
            updateUi(it)
        }
    }

    private fun setTouchEvent() {
        binding.whole.apply {
            setOnClickListener {
                isFocusableInTouchMode = true
            }
            setOnFocusChangeListener { _, _ ->
                viewModel.isUploadFieldFocused.value = false
                val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(this.windowToken, 0);
            }
        }
    }

    private fun setUpFields() {
        listOf(
            "#when" to "시간을 입력해주세요", "#where" to "장소를 입력해주세요", "#who" to "함께한 사람을 입력해주세요", "#what" to "무엇을 했는지 입력해주세요"
        ).forEach {
            ItemUploadFieldBinding.inflate(layoutInflater).apply {
                root.id = ViewCompat.generateViewId()
                title = it.first
                hint = it.second

                // 서버 통신시 해당 코드 변경
                val tagList = listOf("다섯글자임", "학교", "단골카페", "이마트")

                if (tagList.isNotEmpty()) {
                    tagList.forEach { tag ->
                        createChip(tag).apply {
                            setOnClickListener {
                                etContent.setText(tag)
                            }
                            chipGroup.addView(this)
                        }
                    }
                }


                etContent.setOnFocusChangeListener { _, hasFocus ->
                    viewModel.isUploadFieldFocused.value = hasFocus
                    borderField.apply {
                        strokeColor = if (hasFocus) context.getColor(R.color.dark_blue) else Color.TRANSPARENT
                        strokeWidth = if (hasFocus) this@UploadHappicActivity.px(1).toFloat() else 0f
                    }
                    chipGroup.visibility = if (hasFocus) VISIBLE else GONE
                }

                binding.containerFields.addView(this.root)
            }

        }
    }

    private fun createChip(tag: String): Chip {
        val width = (binding.containerFields.width - this.px(47)) / 3
        val chip = ItemUploadChipBinding.inflate(layoutInflater).root.apply {
            text = tag
            layoutParams = ConstraintLayout.LayoutParams(width, WRAP_CONTENT)
        }
        return chip
    }

    private fun updateUi(hasFocus: Boolean) {
        val photoMarginHorizontal = if (hasFocus) 140 else 50
        val containerMarginTop = if (hasFocus) 32 else 20
        binding.ivPhoto.updateLayoutParams<MarginLayoutParams> {
            leftMargin = this@UploadHappicActivity.px(photoMarginHorizontal)
            rightMargin = this@UploadHappicActivity.px(photoMarginHorizontal)
        }
        binding.containerFields.updateLayoutParams<MarginLayoutParams> {
            topMargin = this@UploadHappicActivity.px(containerMarginTop)
        }
    }
}