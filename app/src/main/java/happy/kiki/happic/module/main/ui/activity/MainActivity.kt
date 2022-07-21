package happy.kiki.happic.module.main.ui.activity

import HomeFragment
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import happy.kiki.happic.databinding.ActivityMainBinding
import happy.kiki.happic.module.core.util.PermissionDexterUtil
import happy.kiki.happic.module.core.util.PermissionDexterUtil.PermissionListener
import happy.kiki.happic.module.core.util.extension.addFragment
import happy.kiki.happic.module.core.util.extension.collectFlowWhenStarted
import happy.kiki.happic.module.core.util.extension.isFragmentExist
import happy.kiki.happic.module.core.util.extension.pushActivity
import happy.kiki.happic.module.core.util.extension.showToast
import happy.kiki.happic.module.dailyhappic.ui.activity.GalleryActivity
import happy.kiki.happic.module.dailyhappic.ui.fragment.DailyHappicContainerFragment
import happy.kiki.happic.module.dailyhappic.ui.fragment.DailyHappicViewModel
import happy.kiki.happic.module.report.ui.fragment.ReportContainerFragment
import happy.kiki.happic.module.setting.ui.fragment.SettingFragment
import happy.kiki.happic.module.upload.ui.activity.UploadHappicActivity
import happy.kiki.happic.module.upload.ui.activity.UploadHappicActivity.Argument

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val vm by viewModels<MainViewModel>()
    private val dailyHappicVm by viewModels<DailyHappicViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        configureBottomTab()
        navigateUploadDailyHappic()
    }

    private fun configureBottomTab() {
        binding.bottomTab.onTabSelectedListener = {
            vm.tabIndex.value = it
        }
        collectFlowWhenStarted(vm.tabIndex) {
            when (it) {
                0 -> showFragment<HomeFragment>()
                1 -> showFragment<DailyHappicContainerFragment>()
                2 -> showFragment<ReportContainerFragment>()
                3 -> showFragment<SettingFragment>()
            }
            binding.bottomTab.selectedTabIndex = it
        }
    }

    private val galleryActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) {
        if (it.resultCode == 100) {
            it.data?.getStringExtra("uri")?.let {
                pushActivity<UploadHappicActivity>(Argument(it))
            }
        }
    }

    private fun navigateUploadDailyHappic() {
        binding.bottomTab.setFabClickListener {
            dailyHappicVm.navigateUploadApi.call()
        }
        collectFlowWhenStarted(dailyHappicVm.onNavigateUpload.flow) {
            PermissionDexterUtil().requestPermissions(
                this, object : PermissionListener {
                    override fun onPermissionGranted() {
                        galleryActivityLauncher.launch(Intent(this@MainActivity, GalleryActivity::class.java))
                    }

                    override fun onPermissionShouldBeGranted(deniedPermissions: List<String>) {
                        showToast("갤러리 권한을 허용해주세요")
                    }

                    override fun onAnyPermissionsPermanentlyDeined(
                        deniedPermissions: List<String>, permanentDeniedPermissions: List<String>
                    ) {
                        showToast("갤러리 권한을 [설정] - [앱] 에서허용해주세요")
                    }
                }, listOf(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        }
        collectFlowWhenStarted(dailyHappicVm.onNavigateUploadFailedByMultipleUpload.flow) {
            showToast("하루해픽은 1일 1회 등록만 가능합니다.")
        }
    }

    private inline fun <reified T : Fragment> showFragment() = supportFragmentManager.commit {
        val shouldBeAdded = !isFragmentExist<T>()
        supportFragmentManager.fragments.forEach {
            if (it is T) show(it)
            else hide(it)
        }
        if (shouldBeAdded) addFragment<T>(
            binding.fragmentContainer, tag = T::class.java.name, skipAddToBackStack = true
        )
    }
}