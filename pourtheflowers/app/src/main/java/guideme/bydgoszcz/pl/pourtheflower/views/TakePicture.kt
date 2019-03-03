package guideme.bydgoszcz.pl.pourtheflower.views

import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v4.app.FragmentActivity
import android.support.v4.content.FileProvider
import android.widget.ImageView
import guideme.bydgoszcz.pl.pourtheflower.views.fragments.ImageLoader
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

    fun requestTakePicture(activity: FragmentActivity): File {
        lateinit var photoFile: File
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity.packageManager)?.also {
                photoFile = createImageFile(activity)
                // Continue only if the File was successfully created
                photoFile.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                            activity,
                            "guideme.bydgoszcz.pl.pourtheflower.fileprovider",
                            it
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

    fun setPictureToImageView(imageView: ImageView, photoPicturePath: String) {
        // Get the dimensions of the View
//        val targetW: Int = imageView.width
//        val targetH: Int = imageView.height
//
//        val bmOptions = BitmapFactory.Options().apply {
//            // Get the dimensions of the bitmap
//            inJustDecodeBounds = true
//            BitmapFactory.decodeFile(photoPicturePath, this)
//            val photoW: Int = outWidth
//            val photoH: Int = outHeight
//
//            // Determine how much to scale down the image
//            val scaleFactor: Int = Math.min(photoW / targetW, photoH / targetH)
//
//            // Decode the image file into a Bitmap sized to fill the View
//            inJustDecodeBounds = false
//            inSampleSize = scaleFactor
//            inPurgeable = true
//        }
//        BitmapFactory.decodeFile(photoPicturePath, bmOptions)?.also { bitmap ->
//            ImageLoader
//            imageView.setImageBitmap(bitmap)
//        }
        ImageLoader.setImage(imageView, photoPicturePath, imageView.width, imageView.height, onError = {
            // noop
        })
    }
}