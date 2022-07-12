package happy.kiki.happic.module.main.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import happy.kiki.happic.databinding.FragmentCloudBinding
import happy.kiki.happic.module.core.util.AutoCleardValue

class CloudFragment : Fragment() {
    private var binding by AutoCleardValue<FragmentCloudBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentCloudBinding.inflate(inflater, container, false).let { binding = it; it.root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }
}