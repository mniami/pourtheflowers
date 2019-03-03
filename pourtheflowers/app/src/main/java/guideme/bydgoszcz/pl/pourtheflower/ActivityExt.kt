package guideme.bydgoszcz.pl.pourtheflower

import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AlertDialog
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
        activity.onBackPressedCallback = { true }
        block()
    }
}

fun Fragment.showConfirmationDialog(titleId: Int, messageId: Int, onSuccess: () -> Unit, onFailure: () -> Unit) {
    context ?: return onFailure()
    showConfirmationDialog(getString(titleId), getString(messageId), onSuccess, onFailure)
}

fun Fragment.goBack() {
    val activity = activity as MainActivity? ?: return
    activity.goBack()
}

fun Fragment.returnFalse(): Boolean {
    return false
}

fun Fragment.showConfirmationDialog(title: String, message: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
    context ?: return onFailure()
    AlertDialog.Builder(requireContext())
            .setIcon(android.R.drawable.ic_dialog_info)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(getString(R.string.dialog_button_yes)) { _, _ -> onSuccess() }
            .setNegativeButton(getString(R.string.dialog_button_no)) { _, _ -> onFailure() }
            .show()
}

fun Fragment.showSnack(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(view ?: return, message, duration).show()
}

fun Fragment.showSnack(messageId: Int, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(view ?: return, messageId, duration).show()
}