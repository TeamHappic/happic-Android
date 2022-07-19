package happy.kiki.happic.module.upload.ui.activity

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup.GONE
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.ViewGroup.MarginLayoutParams
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
        configureFields()
        configureGalleryLogic()
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it?.run {
            binding.ivPhoto.setImageURI(this)
        }
    }

    private fun configureGalleryLogic() {
        binding.ivX.setOnClickListener {
            launcher.launch("image/*")
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
                imm.hideSoftInputFromWindow(this.windowToken, 0)
            }
        }
        collectFlowWhenStarted(viewModel.isUploadFieldFocused) {
            updateUi(it)
        }
    }

    private fun configureFields() {
        listOf("#where" to "장소를 입력해주세요", "#who" to "함께한 사람을 입력해주세요", "#what" to "무엇을 했는지 입력해주세요").forEach {
            ItemUploadFieldBinding.inflate(layoutInflater).apply {
                root.id = ViewCompat.generateViewId()
                title = it.first
                hint = it.second

                etContent.setOnFocusChangeListener { _, hasFocus ->
                    viewModel.isUploadFieldFocused.value = hasFocus
                    borderField.apply {
                        strokeColor = if (hasFocus) context.getColor(R.color.dark_blue) else Color.TRANSPARENT
                        strokeWidth = if (hasFocus) this@UploadHappicActivity.px(1).toFloat() else 0f
                    }
                    containerTags.visibility = if (hasFocus) VISIBLE else GONE
                }

                collectFlowWhenStarted(viewModel.dailyHappicKeywordApi.data) {
                    it?.run {
                        llTags.removeAllViews()
                        val tagList = when (title) {
                            "#where" -> where
                            "#who" -> who
                            else -> what
                        }
                        if (tagList.isNotEmpty()) {
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
                binding.containerFields.addView(this.root)
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