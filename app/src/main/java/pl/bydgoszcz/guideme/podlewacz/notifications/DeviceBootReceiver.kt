package pl.bydgoszcz.guideme.podlewacz.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import pl.bydgoszcz.guideme.podlewacz.PourTheFlowerApplication
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceBootReceiver @Inject constructor() : BroadcastReceiver() {
    @Inject
    lateinit var itemAlarmScheduler: ItemAlarmScheduler

    override fun onReceive(context: Context, intent: Intent) {
        (context.applicationContext as PourTheFlowerApplication).component.inject(this)
        Log.w("podlewacz", "[Device boot receiver] onReceive, action: ${intent.action}")
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            itemAlarmScheduler.schedule()
        }
    }
}