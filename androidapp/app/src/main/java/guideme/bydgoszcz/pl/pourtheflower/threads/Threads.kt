package guideme.bydgoszcz.pl.pourtheflower.threads

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executors

fun runOnUi(task: () -> Unit) {
    val handler = Handler(Looper.getMainLooper())
    handler.post { task.invoke() }
}

val backgroundThread = Executors.newSingleThreadExecutor()
fun runInBackground(task: () -> Unit) {
    backgroundThread.submit(task)
}