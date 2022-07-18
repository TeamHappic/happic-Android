package happy.kiki.happic.module.core.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object LocalStorage {
    const val SHARED_PREFERENCE_GLOBAL_KEY = "__APP_SHARED_PREFERENCES__"

    private lateinit var sp: SharedPreferences

    fun install(context: Context) {
        sp = context.getSharedPreferences(SHARED_PREFERENCE_GLOBAL_KEY, 0)
    }

    fun saveString(key: String, value: String) = sp.edit {
        putString(key, value)
    }

    fun getString(key: String) = sp.getString(key, null)

    fun saveInt(key: String, value: Int) = sp.edit {
        putInt(key, value)
    }

    fun getInt(key: String) = if (sp.contains(key)) sp.getInt(key, 0) else null
}