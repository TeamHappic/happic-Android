package happy.kiki.happic.module.setting.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.annotation.DrawableRes
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import happy.kiki.happic.R
import happy.kiki.happic.databinding.DialogCommonBinding
import happy.kiki.happic.module.core.util.AutoCleardValue
import happy.kiki.happic.module.core.util.extension.FRAGMENT_ARGUMENT_KEY_
import happy.kiki.happic.module.core.util.extension.argument
import happy.kiki.happic.module.core.util.extension.px
import happy.kiki.happic.module.core.util.extension.screenWidth
import kotlinx.parcelize.Parcelize
import kotlin.math.roundToInt

class CommonDialog private constructor() : DialogFragment() {
    private var binding by AutoCleardValue<DialogCommonBinding>()

    @Parcelize
    data class Argument(
        @DrawableRes val icon: Int = R.drawable.bell_gray3_20,
        val title: String = "",
        val body: String = "",
        val leftText: String = "",
        val rightText: String = "",
    ) : Parcelable

    private val arg by argument<Argument>()

    private val listener: Listener?
        get() = if (parentFragment == null) (activity as? Listener) else (parentFragment as? Listener)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        DialogCommonBinding.inflate(inflater, container, false).also {
            binding = it
        }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        isCancelable = false
        binding.title.text = arg.title
        binding.body.text = arg.body

        binding.left.text = arg.leftText
        binding.left.setOnClickListener {
            listener?.onClickLeft()
            dismiss()
        }
        binding.right.text = arg.rightText
        binding.right.setOnClickListener {
            listener?.onClickRight()
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()

        val width = (requireContext().screenWidth * 0.9f).coerceAtMost(requireContext().px(320).toFloat())
        dialog?.window?.run {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setDimAmount(0.4f)
            setLayout(width.roundToInt(), LayoutParams.WRAP_CONTENT)
        }
        isCancelable = true
    }

    interface Listener {
        fun onClickLeft() {}
        fun onClickRight() {}
    }

    companion object {
        fun newInstance(arg: Argument) = CommonDialog().apply {
            arguments = bundleOf(FRAGMENT_ARGUMENT_KEY_ to arg)
        }
    }
}