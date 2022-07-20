package happy.kiki.happic.module.auth.util

import happy.kiki.happic.module.core.util.LocalStorage

object JwtUtil {
    private const val KEY_TOKEN = "JWT_TOKEN"
    fun save(token: String) = LocalStorage.saveString(KEY_TOKEN, token)
    fun load() = LocalStorage.getString(KEY_TOKEN)
}

typealias Jwt = String
typealias SnsAccessToken = String