package guideme.bydgoszcz.pl.pourtheflower.views

import android.content.Intent
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v4.app.FragmentActivity
import java.io.File
import java.util.*

class TakePicture {
    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
        val random = Random()
    }

    fun requestTakePicture(activity: FragmentActivity) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->

            val fileUri = File(Environment.getDataDirectory(), random.nextInt().toString() + ".jpg")

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
            takePictureIntent.resolveActivity(activity.packageManager)?.also {
                startActivityForResult(activity, takePictureIntent, REQUEST_IMAGE_CAPTURE, null)
            }
        }
    }
}