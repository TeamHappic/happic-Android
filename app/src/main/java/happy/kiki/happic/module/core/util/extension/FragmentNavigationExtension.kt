package happy.kiki.happic.module.core.util.extension

import android.app.Activity
import android.content.Intent
import android.os.Parcelable
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

const val FRAGMENT_ARGUMENT_KEY_ = "_FRAGMENT_ARGUMENT_KEY_"

inline fun <reified T : Activity> Fragment.pushActivity(
    arg: Parcelable? = null, intentConfig: (Intent) -> Intent = { it },
) = requireActivity().pushActivity<T>(arg, intentConfig)

inline fun <reified T : Fragment> Fragment.addFragment(
    container: ViewGroup,
    arg: Parcelable? = null,
    tag: String? = null,
    skipAddToBackStack: Boolean = false,
    backStackEntryName: String? = null,
    fragmentManager: FragmentManager? = null
) = (fragmentManager ?: parentFragmentManager).commit {
    add(container.id, T::class.java, bundleOf(FRAGMENT_ARGUMENT_KEY_ to arg), tag)

    if (!skipAddToBackStack) addToBackStack(backStackEntryName)
}

inline fun <reified T : Fragment> Fragment.replaceFragment(
    container: ViewGroup,
    arg: Parcelable? = null,
    tag: String? = null,
    skipAddToBackStack: Boolean = false,
    backStackEntryName: String? = null,
    fragmentManager: FragmentManager? = null,
) = (fragmentManager ?: parentFragmentManager).commit {
    replace<T>(container.id, tag, bundleOf(FRAGMENT_ARGUMENT_KEY_ to arg))

    if (!skipAddToBackStack) addToBackStack(backStackEntryName)
}

fun Fragment.popChildBackStack() = childFragmentManager.popBackStack()
fun Fragment.popChildBackStacksUntilNameFound(name: String) = childFragmentManager.popBackStack(name, 0)

inline fun <reified T : Fragment> Fragment.isChildFragmentExistIn() = childFragmentManager.fragments.any { it is T }

class FragmentArgumentDelegate<Arg : Parcelable> : ReadOnlyProperty<Fragment, Arg> {
    override fun getValue(thisRef: Fragment, property: KProperty<*>) =
        thisRef.arguments?.getParcelable<Arg>(FRAGMENT_ARGUMENT_KEY_)
            ?: throw RuntimeException("${this::class.java.simpleName}??? argument??? ???????????? ??? ??????????????????")
}

fun <Arg : Parcelable> Fragment.argument() = FragmentArgumentDelegate<Arg>()