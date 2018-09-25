package guideme.bydgoszcz.pl.pourtheflower.serialization

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.ByteBuffer
import javax.inject.Inject

class DataCache @Inject constructor(private val cacheDir: String) {
    fun save(name: String, buffer: ByteBuffer) {
        val file = File(cacheDir, name)
        FileOutputStream(file, false).channel.use { channel ->
            buffer.flip()
            channel.write(buffer)
        }
    }

    fun load(name: String, buffer: ByteBuffer) {
        val file = File(cacheDir, name)

        if (!file.exists()) {
            return
        }
        FileInputStream(file).channel.use { channel ->
            channel.read(buffer)
        }
    }
}