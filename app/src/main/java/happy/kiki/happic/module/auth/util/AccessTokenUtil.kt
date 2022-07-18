package happy.kiki.happic.module.auth.util

import happy.kiki.happic.module.core.util.LocalStorage

object AccessTokenUtil {
    private const val KEY_TOKEN = "ACCESS_TOKEN"
    fun saveAccessToken(token: String) = LocalStorage.saveString(KEY_TOKEN, token)
    fun getAccessToken() = LocalStorage.getString(KEY_TOKEN)
}