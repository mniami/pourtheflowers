package pl.bydgoszcz.guideme.podlewacz.utils

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.res.ResourcesCompat

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

fun Resources.getDrawableFromResources(drawableId: Int): Drawable? {
    return ResourcesCompat.getDrawable(this, drawableId, null)
}

fun Boolean.toVisibility(): Int {
    return if (this) View.VISIBLE else View.GONE
}