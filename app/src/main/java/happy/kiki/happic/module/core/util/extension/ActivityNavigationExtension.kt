package happy.kiki.happic.module.core.util.extension

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Parcelable
import android.view.View
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
    sharedElementPairs: List<android.util.Pair<View, String>> = listOf()
) = startActivity(
    intentConfig(
        Intent(this, T::class.java),
    ).apply {
        arg?.run { putExtra(_ACTIVITY_ARGUMENT_KEY_, arg) }
    }, if (sharedElementPairs.isNotEmpty()) ActivityOptions.makeSceneTransitionAnimation(
        this, *sharedElementPairs.toTypedArray()
    ).toBundle() else null
)

inline fun <reified T : Activity> Activity.replaceActivity(
    arg: Parcelable? = null,
    intentConfig: IntentConfig = { it },
    sharedElementPairs: List<android.util.Pair<View, String>> = listOf()
) {
    pushActivity<T>(arg, intentConfig, sharedElementPairs)
    finish()
}

inline fun <reified T : Fragment> AppCompatActivity.addFragment(
    container: ViewGroup,
    arg: Parcelable? = null,
    tag: String? = null,
    skipAddToBackStack: Boolean = false,
    backStackEntryName: String? = null,
) = supportFragmentManager.commit {
    add(container.id, T::class.java, bundleOf(FRAGMENT_ARGUMENT_KEY_ to arg), tag)
    if (!skipAddToBackStack) addToBackStack(backStackEntryName)
}

inline fun <reified T : Fragment> AppCompatActivity.replaceFragment(
    container: ViewGroup,
    arg: Parcelable? = null,
    tag: String? = null,
    skipAddToBackStack: Boolean = false,
    backStackEntryName: String? = null
) = supportFragmentManager.commit {
    replace<T>(container.id, tag, bundleOf(FRAGMENT_ARGUMENT_KEY_ to arg))

    if (!skipAddToBackStack) addToBackStack(backStackEntryName)
}

inline fun <reified T : Fragment> AppCompatActivity.isFragmentExist() = supportFragmentManager.fragments.any { it is T }

fun AppCompatActivity.popBackStack() = supportFragmentManager.popBackStack()
fun AppCompatActivity.popBackStacksUntilNameFound(name: String) = supportFragmentManager.popBackStack(name, 0)

class ActivityArgumentDelegate<Arg : Parcelable> : ReadOnlyProperty<AppCompatActivity, Arg> {
    override fun getValue(thisRef: AppCompatActivity, property: KProperty<*>) =
        thisRef.intent?.getParcelableExtra<Arg>(_ACTIVITY_ARGUMENT_KEY_)
            ?: throw RuntimeException("${this::class.java.simpleName}??? argument??? ???????????? ??? ??????????????????")
}

fun <Arg : Parcelable> AppCompatActivity.argument() = ActivityArgumentDelegate<Arg>()