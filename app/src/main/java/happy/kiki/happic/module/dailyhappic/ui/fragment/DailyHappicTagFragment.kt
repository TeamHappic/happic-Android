package happy.kiki.happic.module.dailyhappic.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import happy.kiki.happic.databinding.FragmentDailyHappicTagBinding
import happy.kiki.happic.module.core.util.AutoCleardValue

class DailyHappicTagFragment : Fragment() {
    private var binding by AutoCleardValue<FragmentDailyHappicTagBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentDailyHappicTagBinding.inflate(inflater, container, false).let { binding = it; it.root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }
}