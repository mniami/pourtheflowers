package guideme.bydgoszcz.pl.pourtheflower

import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.WindowManager
import guideme.bydgoszcz.pl.pourtheflower.dagger.AppComponent

inline fun <reified T : Fragment> T.injector(d: AppComponent.() -> Unit) {
    val component = (activity?.application as PourTheFlowerApplication).component
    component.apply(d)
}

fun FragmentActivity.goBack() {
    window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    supportFragmentManager?.popBackStack()
}

fun Fragment.doOnBackPressed(block: () -> Boolean) {
    val activity = activity as MainActivity? ?: return
    activity.onBackPressedCallback = {
        if (block()) {
            activity.onBackPressedCallback = { true }
            true
        } else {
            false
        }
    }
}

fun Fragment.showSnack(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(view ?: return, message, duration).show()
}

fun Fragment.showSnack(messageId: Int, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(view ?: return, messageId, duration).show()
}