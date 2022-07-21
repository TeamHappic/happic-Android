package happy.kiki.happic.module.setting.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.fragment.app.DialogFragment
import happy.kiki.happic.databinding.DialogCharacterUpdateNudgeBinding
import happy.kiki.happic.module.core.util.AutoCleardValue
import happy.kiki.happic.module.core.util.extension.px
import happy.kiki.happic.module.core.util.extension.screenWidth
import kotlin.math.roundToInt

class CharacterUpdateNudgeDialog private constructor() : DialogFragment() {
    private var binding by AutoCleardValue<DialogCharacterUpdateNudgeBinding>()

    private val listener: Listener?
        get() = if (parentFragment == null) (activity as? Listener) else (parentFragment as? Listener)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        DialogCharacterUpdateNudgeBinding.inflate(inflater, container, false).also {
            binding = it
        }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        isCancelable = false
        binding.cancel.setOnClickListener {
            listener?.onClickCancel()
            dismiss()
        }
        binding.change.setOnClickListener {
            listener?.onClickChange()
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
        fun onClickChange() {}
        fun onClickCancel() {}
    }

    companion object {
        fun newInstance() = CharacterUpdateNudgeDialog()
    }
}