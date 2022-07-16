package happy.kiki.happic.module.upload.ui.activity

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup.MarginLayoutParams
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.updateLayoutParams
import happy.kiki.happic.R
import happy.kiki.happic.databinding.ActivityUploadHappicBinding
import happy.kiki.happic.databinding.ItemUploadFieldBinding
import happy.kiki.happic.module.core.util.extension.px
import happy.kiki.happic.module.upload.data.UploadFieldModel

class UploadHappicActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadHappicBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityUploadHappicBinding.inflate(layoutInflater).also { binding = it;setContentView(it.root) }
        setUpFields()
    }

    private fun setUpFields() {
        listOf(
            Pair("#when", "시간을 입력해주세요"),
            Pair("#where", "장소를 입력해주세요"),
            Pair("#who", "함께한 사람을 입력해주세요"),
            Pair("#what", "무엇을 했는지 입력해주세요"),
        ).forEach {
            ItemUploadFieldBinding.inflate(layoutInflater).apply {
                root.id = ViewCompat.generateViewId()
                title = it.first
                hint = it.second

                this.etContent.setOnFocusChangeListener(object : OnFocusChangeListener {
                    override fun onFocusChange(view: View?, hasFocus: Boolean) {
                        if (hasFocus) {
                            changeImageViewMargin(140)
                            this@apply.borderField.apply{
                                strokeColor = context.getColor(R.color.dark_blue)
                                strokeWidth = this@UploadHappicActivity.px(1).toFloat()
                            }
                        } else {
                            changeImageViewMargin(50)
                            this@apply.borderField.apply{
                                strokeColor = Color.TRANSPARENT
                                strokeWidth = 0f
                            }
                        }
                    }
                })

                binding.containerFields.addView(this.root)
            }

        }
    }

    private fun changeImageViewMargin(dp: Int) {
        binding.ivPhoto.updateLayoutParams<MarginLayoutParams> {
            leftMargin = this@UploadHappicActivity.px(dp)
            rightMargin = this@UploadHappicActivity.px(dp)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    v.clearFocus()
                    val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0);
                }
            }
        }

        return super.dispatchTouchEvent(ev)
    }
}