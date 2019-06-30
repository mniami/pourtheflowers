package pl.bydgoszcz.guideme.podlewacz.views.fragments

import android.os.Handler

class Worker(val isStopped: () -> Boolean, val holder: FlowerRecyclerViewAdapter.ViewHolder) {
    private val REFRESH_DELAY = 5000L
    private val handler: Handler = Handler()

    var onTick: (FlowerRecyclerViewAdapter.ViewHolder) -> Unit = {}

    fun run() {
        if (isStopped()) {
            return
        }
        handler.postDelayed({
            onTick(holder)
            run()
        }, REFRESH_DELAY)
    }

    companion object {
        fun constructAndRun(isStopped: () -> Boolean, holder: FlowerRecyclerViewAdapter.ViewHolder): Worker {
            val worker = Worker(isStopped, holder)
            worker.run()
            return worker
        }
    }
}