package guideme.bydgoszcz.pl.pourtheflower.utils

import kotlinx.coroutines.*

fun launchUI(block: suspend CoroutineScope.() -> Unit) = GlobalScope.launch(Dispatchers.Main, block = block)

fun launchIO(block: suspend CoroutineScope.() -> Unit) = GlobalScope.launch(Dispatchers.IO, block = block)

fun <T> asyncIO(block: suspend CoroutineScope.() -> T) = GlobalScope.async(Dispatchers.IO, block = block)

fun <T> CoroutineScope.asyncIO(block: suspend CoroutineScope.() -> T) = async(Dispatchers.IO, block = block)

suspend fun <T> offload(block: suspend CoroutineScope.() -> T) = withContext(Dispatchers.IO, block = block)
