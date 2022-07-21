package happy.kiki.happic.module.main.ui.activity

import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import happy.kiki.happic.module.core.data.api.base.useApi
import happy.kiki.happic.module.push.data.api.PushService.RegisterFcmTokenReq
import happy.kiki.happic.module.push.data.api.pushService
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel : ViewModel() {
    val tabIndex = MutableStateFlow(0)

    private val fcmRegisterApi = useApi<RegisterFcmTokenReq> {
        pushService.registerFcmToken(it)
    }

    init {
        registerFcmToken()
    }

    private fun registerFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) return@OnCompleteListener
            fcmRegisterApi.call(RegisterFcmTokenReq(task.result))
        })
    }
}