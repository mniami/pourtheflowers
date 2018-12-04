package guideme.bydgoszcz.pl.pourtheflower.threads

import android.os.Handler
import android.os.Looper
import guideme.bydgoszcz.pl.pourtheflower.utils.asyncIO
import java.util.concurrent.Executors

fun runOnUi(task: () -> Unit) {
    val handler = Handler(Looper.getMainLooper())
    handler.post { task.invoke() }
}

fun runInBackground(task: () -> Unit): ThreadHandler {
    val threadHandler = ThreadHandler()
    asyncIO {
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
