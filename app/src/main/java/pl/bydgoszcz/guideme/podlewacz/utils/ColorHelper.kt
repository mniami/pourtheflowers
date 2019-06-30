package pl.bydgoszcz.guideme.podlewacz.utils

import android.content.Context

class ColorHelper {
    companion object {
        fun getColor(colorResource: Int, context: Context): Int {
            return context.resources.getColorFromResource(colorResource)
        }
    }
}