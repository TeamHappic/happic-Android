package happy.kiki.happic.module.setting.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import happy.kiki.happic.BuildConfig
import happy.kiki.happic.R
import happy.kiki.happic.databinding.FragmentSettingBinding
import happy.kiki.happic.module.characterselect.provider.CharacterSelectFlowProvider
import happy.kiki.happic.module.characterselect.ui.activity.CharacterActivity
import happy.kiki.happic.module.core.util.AutoCleardValue
import happy.kiki.happic.module.core.util.extension.pushActivity
import happy.kiki.happic.module.setting.ui.dialog.CommonDialog
import happy.kiki.happic.module.setting.ui.dialog.CommonDialog.Argument

class SettingFragment : Fragment(), CommonDialog.Listener {
    private var binding by AutoCleardValue<FragmentSettingBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentSettingBinding.inflate(inflater, container, false).let { binding = it; it.root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        configureNavigations()
        setAppVersionText()
    }

    private fun configureNavigations() {
        binding.characterChange.root.setOnClickListener {
            CommonDialog.newInstance(
                Argument(
                    icon = R.drawable.bell_gray3_20,
                    title = "캐릭터 변경 주의사항",
                    body = "변경 시 현재 캐릭터 성장이\n초기화됩니다.\n정말 변경하시겠습니까?",
                    leftText = "취소",
                    rightText = "캐릭터 변경",
                )
            ).show(childFragmentManager, null)
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

    override fun onClickLeft() {
        CharacterSelectFlowProvider.initForUpdate()
        pushActivity<CharacterActivity>()
    }

    private fun setAppVersionText() = binding.version.apply {
        text = "${BuildConfig.VERSION_NAME}(${BuildConfig.VERSION_CODE})"
    }
}