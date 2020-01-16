package pl.bydgoszcz.guideme.podlewacz.utils

import java.io.File
import java.io.IOException

fun runProcess(command: String, workingDir: File? = null): String? {
    return try {
        val parts = command.split("\\s".toRegex())
        val proc = ProcessBuilder(*parts.toTypedArray())
                .directory(workingDir)
                .start()
        proc.waitFor()
        proc.inputStream.bufferedReader().readText()
    } catch(e: IOException) {
        null
    }
}

fun runGetDumpsysAlarm() : String? {
    return runProcess("dumpsys alarm | grep podlewacz")
}