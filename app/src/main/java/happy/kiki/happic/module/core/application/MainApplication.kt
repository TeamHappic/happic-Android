package happy.kiki.happic.module.core.application

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import happy.kiki.happic.module.core.util.LocalStorage
import happy.kiki.happic.module.core.util.setUpFlipper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        setUpFlipper()
        setUpKakaoSDK()
        LocalStorage.install(this)
    }

    private fun setUpKakaoSDK() {
        KakaoSdk.init(this, "87cf2952b41116b741f021967c8add1f")
    }

    companion object {
        val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    }
}