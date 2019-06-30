package pl.bydgoszcz.guideme.podlewacz.views

import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object TakePicture {
    const val REQUEST_IMAGE_CAPTURE = 1

    @Throws(IOException::class)
    private fun createImageFile(activity: FragmentActivity): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        )
    }


    fun requestTakePicture(activity: FragmentActivity?): File? {
        var photoFile: File? = null
        val activity = activity
                ?: throw IllegalStateException("Request take picture has no activity")

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity.packageManager)?.also {
                photoFile = createImageFile(activity)

                // Continue only if the File was successfully created
                photoFile.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                            activity,
                            "${activity.packageName}.fileprovider",
                            it ?: return@also
                    )
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                        takePictureIntent.clipData = ClipData.newRawUri("", photoURI)
                        takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(activity, takePictureIntent, REQUEST_IMAGE_CAPTURE, null)
                }
            }
        }
        return photoFile
    }
}