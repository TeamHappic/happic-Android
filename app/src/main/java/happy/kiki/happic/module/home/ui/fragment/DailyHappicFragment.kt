import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import happy.kiki.happic.databinding.FragmentDailyHappicBinding
import happy.kiki.happic.module.core.util.AutoCleardValue
import happy.kiki.happic.module.core.util.extension.addFragment
import happy.kiki.happic.module.core.util.extension.pushActivity
import happy.kiki.happic.module.core.util.extension.replaceFragment
import happy.kiki.happic.module.upload.ui.activity.UploadHappicActivity

class DailyHappicFragment : Fragment() {
    private var binding by AutoCleardValue<FragmentDailyHappicBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentDailyHappicBinding.inflate(inflater, container, false).let { binding = it; it.root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        attachFragment()
    }

    private fun attachFragment() {
        pushActivity<UploadHappicActivity> ()
    }
}