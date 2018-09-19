package guideme.bydgoszcz.pl.pourtheflower.utils

import android.view.View

inline fun <T : View> T.afterMeasured(crossinline f: T.() -> Unit) {
    postDelayed({
        if (measuredWidth > 0 && measuredHeight > 0) {
            f()
        }
    }, 50)
}