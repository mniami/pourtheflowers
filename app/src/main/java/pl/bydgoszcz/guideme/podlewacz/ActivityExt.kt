package pl.bydgoszcz.guideme.podlewacz

import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar
import pl.bydgoszcz.guideme.podlewacz.dagger.AppComponent
import kotlin.math.abs

inline fun <reified T : Fragment> T.injector(d: AppComponent.() -> Unit) {
    val component = (activity?.application as PourTheFlowerApplication).component
    component.apply(d)
}

fun FragmentActivity.goBack() {
    window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    supportFragmentManager?.popBackStack()
}

fun Fragment.showConfirmationDialog(titleId: Int, messageId: Int, onSuccess: () -> Unit, onFailure: () -> Unit) {
    context ?: return onFailure()
    showConfirmationDialog(getString(titleId), getString(messageId), onSuccess, onFailure)
}

fun Fragment.goBack() {
    val activity = activity as MainActivity? ?: return
    activity.goBack()
}

fun Any.returnTrue(): Boolean {
    return true
}

fun Any.returnFalse(): Boolean {
    return true
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
fun View.onClick(action: ()->Unit) {
    var eventDown = Pair(0f, 0f)
    setOnTouchListener { v, event ->
        if (event.action == MotionEvent.ACTION_DOWN) {
            eventDown = Pair(event.x, event.y)
        }
        else if (event.action == MotionEvent.ACTION_UP) {
            val mouseMoveRange = v.width * 0.05f
            if (abs(event.x - eventDown.first) < mouseMoveRange &&
                    abs(event.y - eventDown.second) < mouseMoveRange) {
                action()
                returnTrue()
            }
        }
        returnFalse()
    }
}