package happy.kiki.happic.module.push.data.api

import com.google.firebase.messaging.FirebaseMessagingService
import happy.kiki.happic.module.core.application.MainApplication
import happy.kiki.happic.module.core.data.api.base.useApi
import happy.kiki.happic.module.push.data.api.PushService.RegisterFcmTokenReq

class FCMService : FirebaseMessagingService() {
    private val fcmRegisterApi = useApi<RegisterFcmTokenReq, Unit>(MainApplication.applicationScope) {
        pushService.registerFcmToken(it)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        fcmRegisterApi.call(RegisterFcmTokenReq(token))
    }
}