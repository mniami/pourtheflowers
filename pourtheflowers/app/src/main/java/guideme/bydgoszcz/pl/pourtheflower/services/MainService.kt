package guideme.bydgoszcz.pl.pourtheflower.services

import android.app.Service
import android.content.Intent
import android.os.IBinder

class MainService : Service() {
    private var mBinder: IBinder? = null

    override fun onBind(intent: Intent?): IBinder? {
        return mBinder
    }
}