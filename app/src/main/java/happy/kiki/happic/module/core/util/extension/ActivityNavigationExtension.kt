package happy.kiki.happic.module.core.util.extension

import android.app.Activity
import android.content.Intent
import android.os.Parcelable
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

const val _ACTIVITY_ARGUMENT_KEY_ = "_ACTIVITY_ARGUMENT_KEY_"

typealias IntentConfig = (Intent) -> Intent

inline fun <reified T : Activity> Activity.pushActivity(
    arg: Parcelable? = null,
    intentConfig: IntentConfig = { it },
) = startActivity(intentConfig(Intent(this, T::class.java)).apply {
    arg?.run { putExtra(_ACTIVITY_ARGUMENT_KEY_, arg) }
})

inline fun <reified T : Activity> Activity.replaceActivity(
    arg: Parcelable? = null, intentConfig: IntentConfig = { it }
) {
    pushActivity<T>(arg, intentConfig)
    finish()
}

inline fun <reified T : Fragment> AppCompatActivity.addFragment(
    container: ViewGroup,
    arg: Parcelable? = null,
    tag: String? = null,
    skipAddToBackStack: Boolean = false,
    backStackEntryName: String? = null,
) = supportFragmentManager.commit {
    add(container.id, T::class.java, bundleOf(_FRAGMENT_ARGUMENT_KEY_ to arg), tag)
    if (!skipAddToBackStack) addToBackStack(backStackEntryName)
}

inline fun <reified T : Fragment> AppCompatActivity.replaceFragment(
    container: ViewGroup,
    arg: Parcelable? = null,
    tag: String? = null,
    skipAddToBackStack: Boolean = false,
    backStackEntryName: String? = null
) = supportFragmentManager.commit {
    replace<T>(container.id, tag, bundleOf(_FRAGMENT_ARGUMENT_KEY_ to arg))

    if (!skipAddToBackStack) addToBackStack(backStackEntryName)
}

inline fun <reified T : Fragment> AppCompatActivity.isFragmentExist() = supportFragmentManager.fragments.any { it is T }

fun AppCompatActivity.popBackStack() = supportFragmentManager.popBackStack()
fun AppCompatActivity.popBackStacksUntilNameFound(name: String) = supportFragmentManager.popBackStack(name, 0)

class ActivityArgumentDelegate<Arg : Parcelable> : ReadOnlyProperty<AppCompatActivity, Arg> {
    override fun getValue(thisRef: AppCompatActivity, property: KProperty<*>) =
        thisRef.intent?.getParcelableExtra<Arg>(_ACTIVITY_ARGUMENT_KEY_)
            ?: throw RuntimeException("${this::class.java.simpleName}이 argument를 가져오는 데 실패했습니다")
}

fun <Arg : Parcelable> AppCompatActivity.argument() = ActivityArgumentDelegate<Arg>()