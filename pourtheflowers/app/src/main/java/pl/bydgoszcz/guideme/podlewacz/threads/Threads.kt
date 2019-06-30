package pl.bydgoszcz.guideme.podlewacz.threads

import android.os.Handler
import android.os.Looper
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

private val uiHandler: Handler = Handler(Looper.getMainLooper())
private val executeService: ExecutorService = Executors.newSingleThreadExecutor()

fun runOnUi(task: () -> Unit) {
    uiHandler.post { task.invoke() }
}

fun runInBackground(task: () -> Unit): ThreadHandler {
    val threadHandler = ThreadHandler()
    executeService.submit {
        try {
            task()
        } catch (ex: Exception) {
            threadHandler.fireError(ex)
        }
    }
    return threadHandler
}

class ThreadHandler {
    private var errorHandler: (Exception) -> Unit = {}

    fun onError(handler: (Exception) -> Unit) {
        errorHandler = handler
    }

    fun fireError(ex: Exception) {
        errorHandler(ex)
    }
}
