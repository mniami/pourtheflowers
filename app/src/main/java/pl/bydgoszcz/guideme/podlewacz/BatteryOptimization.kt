package pl.bydgoszcz.guideme.podlewacz

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.PowerManager
import android.provider.Settings

object BatteryOptimization {
    fun check(context: Context) {
        val intent = Intent()
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.M) {
            if (!pm.isIgnoringBatteryOptimizations(context.packageName)) {
                intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                intent.data = Uri.parse("package:${context.packageName}")
                context.startActivity(intent)
            }
        }
    }
}