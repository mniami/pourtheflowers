package pl.bydgoszcz.guideme.podlewacz.utils

class ByteHex {
    private val hexArray = "0123456789ABCDEF".toCharArray()

    object Singleton {
        val Instance = ByteHex()
    }

    fun bytesToHex(bytes: ByteArray): String {
        val hexChars = CharArray(bytes.size * 2)
        for (j in 0 until bytes.size) {
            val v = bytes[j].toInt() and 0xFF
            hexChars[j * 2] = hexArray[v ushr 4]
            hexChars[j * 2 + 1] = hexArray[v and 0x0F]
        }
        return String(hexChars)
    }

    fun hexStringToByteArray(value: String): ByteArray {
        val result = ByteArray(value.length / 2)

        for (i in 0 until value.length step 2) {
            val firstIndex = hexArray.indexOf(value[i])
            val secondIndex = hexArray.indexOf(value[i + 1])

            val octet = firstIndex.shl(4).or(secondIndex)
            result[i.shr(1)] = octet.toByte()
        }

        return result
    }
}