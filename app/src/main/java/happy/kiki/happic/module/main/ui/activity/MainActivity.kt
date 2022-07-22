package happy.kiki.happic.module.main.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import happy.kiki.happic.databinding.ActivityMainBinding
import happy.kiki.happic.module.core.util.PermissionDexterUtil
import happy.kiki.happic.module.core.util.PermissionDexterUtil.PermissionListener
import happy.kiki.happic.module.core.util.extension.collectFlowWhenStarted
import happy.kiki.happic.module.core.util.extension.pushActivity
import happy.kiki.happic.module.core.util.extension.showToast
import happy.kiki.happic.module.dailyhappic.ui.activity.GalleryActivity
import happy.kiki.happic.module.dailyhappic.ui.fragment.DailyHappicViewModel
import happy.kiki.happic.module.upload.ui.activity.UploadHappicActivity
import happy.kiki.happic.module.upload.ui.activity.UploadHappicActivity.Argument

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val vm by viewModels<MainViewModel>()
    private val dailyHappicVm by viewModels<DailyHappicViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        configurePager()
        configureBottomTab()
        configureUploadNavigation()
    }

    private fun configurePager() {
        binding.pager.run {
            adapter = MainAdapter(this@MainActivity)
            isUserInputEnabled = false
            registerOnPageChangeCallback(object : OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    vm.tabIndex.value = position
                }
            })
        }
    }

    private fun configureBottomTab() {
        binding.bottomTab.onTabSelectedListener = {
            vm.tabIndex.value = it
        }
        collectFlowWhenStarted(vm.tabIndex) {
            binding.pager.setCurrentItem(it, false)
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

    private fun configureUploadNavigation() {
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
}