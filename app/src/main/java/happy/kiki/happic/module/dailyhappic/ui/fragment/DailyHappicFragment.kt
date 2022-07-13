package happy.kiki.happic.module.dailyhappic.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import happy.kiki.happic.databinding.FragmentDailyHappicBinding
import happy.kiki.happic.databinding.ItemDailyHappicPhotoBinding
import happy.kiki.happic.databinding.ItemDailyHappicTagBinding
import happy.kiki.happic.module.core.util.AutoCleardValue
import happy.kiki.happic.module.core.util.extension.addFragment
import happy.kiki.happic.module.core.util.extension.px
import happy.kiki.happic.module.core.util.extension.screenWidth
import happy.kiki.happic.module.dailyhappic.data.PhotoModel
import happy.kiki.happic.module.dailyhappic.data.TagModel

class DailyHappicFragment : Fragment() {
    private var binding by AutoCleardValue<FragmentDailyHappicBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentDailyHappicBinding.inflate(inflater, container, false).let { binding = it; it.root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        attachFragment()
    }

    private fun attachFragment() {
        addFragment<DailyHappicTagFragment>(binding.fragmentContainer, skipAddToBackStack = true)
    }


}