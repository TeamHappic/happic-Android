import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import happy.kiki.happic.databinding.FragmentSettingBinding
import happy.kiki.happic.module.core.util.AutoCleardValue

class SettingFragment : Fragment() {
    private var binding by AutoCleardValue<FragmentSettingBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentSettingBinding.inflate(inflater, container, false).let { binding = it; it.root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }
}