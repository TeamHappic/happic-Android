import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import happy.kiki.happic.databinding.FragmentSettingBinding
import happy.kiki.happic.module.core.util.AutoCleardValue
import happy.kiki.happic.module.core.util.extension.pushActivity

class SettingFragment : Fragment() {
    private var binding by AutoCleardValue<FragmentSettingBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentSettingBinding.inflate(inflater, container, false).let { binding = it; it.root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        configureNavigations()
    }

    private fun configureNavigations() {
        binding.characterChange.root.setOnClickListener {

        }
        binding.termsOfUse.root.setOnClickListener {

        }
        binding.privacyPolicy.root.setOnClickListener {

        }
        binding.opensourceLicense.root.setOnClickListener {
            pushActivity<OssLicensesMenuActivity>()
        }
        binding.developers.root.setOnClickListener {

        }
    }
}