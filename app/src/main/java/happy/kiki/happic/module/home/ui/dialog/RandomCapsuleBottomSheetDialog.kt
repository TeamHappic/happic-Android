package happy.kiki.happic.module.home.ui.dialog

import android.animation.ObjectAnimator
import android.app.Dialog
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.animation.doOnRepeat
import androidx.core.os.bundleOf
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.activityViewModels
import androidx.transition.ChangeBounds
import androidx.transition.ChangeClipBounds
import androidx.transition.ChangeImageTransform
import androidx.transition.ChangeTransform
import androidx.transition.Fade
import androidx.transition.Scene
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import happy.kiki.happic.R
import happy.kiki.happic.databinding.DialogRandomCapsuleBinding
import happy.kiki.happic.databinding.SceneRandomCapsule1Binding
import happy.kiki.happic.databinding.SceneRandomCapsule2Binding
import happy.kiki.happic.module.core.util.AutoClearedValue
import happy.kiki.happic.module.core.util.emitEvent
import happy.kiki.happic.module.core.util.extension.FRAGMENT_ARGUMENT_KEY_
import happy.kiki.happic.module.core.util.extension.argument
import happy.kiki.happic.module.core.util.extension.px
import happy.kiki.happic.module.core.util.extension.screenHeight
import happy.kiki.happic.module.core.util.loadUrlAsync
import happy.kiki.happic.module.dailyhappic.ui.fragment.DailyHappicViewModel
import happy.kiki.happic.module.home.data.model.RandomCapsuleModel
import happy.kiki.happic.module.home.ui.fragment.HomeViewModel
import happy.kiki.happic.module.report.util.koFormat
import happy.kiki.happic.module.report.util.yearMonthDateFormat
import kotlinx.parcelize.Parcelize

class RandomCapsuleBottomSheetDialog private constructor() : BottomSheetDialogFragment() {
    @Parcelize
    data class Argument(val model: RandomCapsuleModel) : Parcelable

    private val arg by argument<Argument>()

    private var binding by AutoClearedValue<DialogRandomCapsuleBinding>()

    private val vm by activityViewModels<HomeViewModel>()
    private val dailyHappicVm by activityViewModels<DailyHappicViewModel>()

    val scene2 by lazy {
        Scene.getSceneForLayout(binding.sceneContainer, R.layout.scene_random_capsule2, requireContext())
    }

    val scene1 by lazy {
        Scene.getSceneForLayout(binding.sceneContainer, R.layout.scene_random_capsule1, requireContext())
    }
    private val behavior get() = BottomSheetBehavior.from(requireView().parent as View)

    override fun getTheme(): Int = R.style.RandomCapsuleBottomSheetDialog
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme).also {
            it.setOnShowListener {
                setHeight(it as BottomSheetDialog)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogRandomCapsuleBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setHeight(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet =
            bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)!!
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)

        bottomSheet.updateLayoutParams {
            height = requireContext().screenHeight - requireContext().px(36)
        }
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        behavior.skipCollapsed = true
        configureSceneTransition()
    }

    private fun configureSceneTransition() {
        val transition = TransitionSet().apply {
            addTransition(Fade())
            addTransition(ChangeBounds())
            addTransition(ChangeClipBounds())
            addTransition(ChangeImageTransform())
            addTransition(ChangeTransform())
        }

        TransitionManager.go(scene1, transition)
        SceneRandomCapsule1Binding.bind(scene1.sceneRoot.getChildAt(0)).run {
            button.setOnClickListener {
                button.isLoading = true
                ObjectAnimator.ofFloat(binding.fadeContainer, "alpha", 0f, 0.7f).apply {
                    repeatMode = ObjectAnimator.REVERSE
                    repeatCount = 1
                    duration = 1500L
                    doOnRepeat {
                        TransitionManager.go(scene2, transition)
                        SceneRandomCapsule2Binding.bind(scene2.sceneRoot.getChildAt(0)).run {
                            img.loadUrlAsync(arg.model.photo)
                            date.text = arg.model.date.toLocalDate().yearMonthDateFormat
                            hour.text = "#${arg.model.hour.koFormat}"
                            where.text = "#${arg.model.where}"
                            who.text = "#${arg.model.who}"
                            what.text = "#${arg.model.what}"

                            val isPosted = vm.homeApi.data.value?.isPosted == true

                            button.text = if (isPosted) "이달의 해픽 돌아보기" else "하루해픽 등록하기"
                            button.setOnClickListener {
                                if (!isPosted) {
                                    emitEvent(dailyHappicVm.onNavigateUpload)
                                }
                                dismiss()
                            }
                        }
                    }
                }.start()
            }
        }
    }

    companion object {
        fun newInstance(model: RandomCapsuleModel) = RandomCapsuleBottomSheetDialog().apply {
            arguments = bundleOf(FRAGMENT_ARGUMENT_KEY_ to Argument(model))
        }
    }
}