package guideme.bydgoszcz.pl.pourtheflower

import android.content.Intent
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v4.app.FragmentActivity

class TakingPicture {
    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
    }

    fun requestTakePicture(activity: FragmentActivity) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity.packageManager)?.also {
                startActivityForResult(activity, takePictureIntent, REQUEST_IMAGE_CAPTURE, null)
            }
        }
    }
}