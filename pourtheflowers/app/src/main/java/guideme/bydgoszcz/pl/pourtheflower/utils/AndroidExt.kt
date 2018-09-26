package guideme.bydgoszcz.pl.pourtheflower.utils

import android.content.res.Resources
import android.support.v4.content.res.ResourcesCompat
import android.view.View

fun <T : View> T.afterMeasured(f: T.() -> Unit) {
    postDelayed({
        if (measuredWidth > 0 && measuredHeight > 0) {
            f()
        }
    }, 50)
}

fun ByteArray.toHex(): String {
    return ByteHex.Singleton.Instance.bytesToHex(this)
}

fun String.fromHexToByteArray(): ByteArray {
    return ByteHex.Singleton.Instance.hexStringToByteArray(this)
}

fun Resources.getColorFromResource(colorId: Int): Int {
    return ResourcesCompat.getColor(this, colorId, null)
}