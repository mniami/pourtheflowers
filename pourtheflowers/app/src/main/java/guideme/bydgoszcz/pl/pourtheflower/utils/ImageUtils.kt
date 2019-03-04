package guideme.bydgoszcz.pl.pourtheflower.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object ImageUtils {
    @Throws(IOException::class)
    fun compressBitmapFile(context: Context?, path: String, proportion: Float, scaleImageSize: Int = 500, onSuccess: () -> Unit) {
        if (context == null)
            throw NullPointerException("Context must not be null.")

        scaleImage(path, scaleImageSize, scaleImageSize) { scaledImage ->
            cropImage(scaledImage, proportion) { croppedImage ->
                saveToFile(croppedImage, path) {
                    onSuccess()
                }
            }
        }
    }

    private fun saveToFile(bitmap: Bitmap, path: String, onSuccess: () -> Unit) {
        val filePath = File(path)
        val fileOutputStream = FileOutputStream(filePath)
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream)
        } finally {
            fileOutputStream.flush()
            fileOutputStream.close()
            bitmap.recycle()
        }
        onSuccess()
    }

    private fun cropImage(srcBmp: Bitmap, proportion: Float, onSuccess: (Bitmap) -> Unit) {
        var dstBitmap: Bitmap
        try {
            if (srcBmp.width >= srcBmp.height) {
                val newWidth = (srcBmp.height * proportion).toInt()
                val offset = ((srcBmp.width - newWidth) * 0.2f).toInt()
                dstBitmap = Bitmap.createBitmap(
                        srcBmp,
                        offset,
                        0,
                        newWidth,
                        srcBmp.height
                )
            } else {
                val newHeight = (srcBmp.width / proportion).toInt()
                val offset = ((srcBmp.height - newHeight) * 0.2f).toInt()
                dstBitmap = Bitmap.createBitmap(
                        srcBmp,
                        0,
                        offset,
                        srcBmp.width,
                        newHeight
                )
            }
        } finally {
            srcBmp.recycle()
        }
        onSuccess(dstBitmap)
    }

    private fun scaleImage(path: String, width: Int, height: Int, onSuccess: (Bitmap) -> Unit) {
        val scaleOptions = BitmapFactory.Options()
        scaleOptions.inJustDecodeBounds = true

        BitmapFactory.decodeFile(path, scaleOptions)

        var scale = 1
        while (scaleOptions.outWidth / scale >= width && scaleOptions.outHeight / scale >= height) {
            scale *= 2
        }

        // decode with the sample size
        val outOptions = BitmapFactory.Options()
        outOptions.inSampleSize = scale

        onSuccess(BitmapFactory.decodeFile(path, outOptions))
    }
}