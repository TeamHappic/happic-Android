package happy.kiki.happic.module.home.ui.dialog

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import happy.kiki.happic.module.home.data.model.RandomCapsuleModel

class RandomCapsuleBottomSheetDialog private constructor() : BottomSheetDialogFragment() {

    companion object {
        fun newInstance(model: RandomCapsuleModel) = RandomCapsuleBottomSheetDialog()
    }
}