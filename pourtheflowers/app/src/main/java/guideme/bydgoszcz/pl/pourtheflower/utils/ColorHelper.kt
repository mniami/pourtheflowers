package guideme.bydgoszcz.pl.pourtheflower.utils

import android.content.Context

class ColorHelper {
    companion object {
        fun getColor(colorResource: Int, context: Context): Int {
            return context.resources.getColorFromResource(colorResource)
        }
    }
}