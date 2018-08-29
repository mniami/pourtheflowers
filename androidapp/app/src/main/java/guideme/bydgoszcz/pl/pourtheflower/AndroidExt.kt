package guideme.bydgoszcz.pl.pourtheflower

import android.os.Build
import android.view.View
import android.view.ViewTreeObserver

inline fun <T : View> T.afterMeasured(crossinline f: T.() -> Unit) {
    postDelayed({
        if (measuredWidth > 0 && measuredHeight > 0) {
            f()
        }
    }, 50)
}