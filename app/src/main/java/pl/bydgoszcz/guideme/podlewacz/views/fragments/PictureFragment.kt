package pl.bydgoszcz.guideme.podlewacz.views.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import pl.bydgoszcz.guideme.podlewacz.R
import pl.bydgoszcz.guideme.podlewacz.databinding.FragmentFlowerEditBinding
import pl.bydgoszcz.guideme.podlewacz.showSnack
import pl.bydgoszcz.guideme.podlewacz.utils.ImageUtils
import pl.bydgoszcz.guideme.podlewacz.views.TakePicture

abstract class PictureFragment : Fragment(), TakingPictureThumbnail {
    protected var photoFilePath: String? = null
    protected var _binder: FragmentFlowerEditBinding? = null
    protected val binder get() = _binder!!

    companion object {
        const val MY_CAMERA_REQUEST_CODE = 100
        const val TAG = "PictureFragment"
    }

    protected fun requestTakePicture() {
        val activity = activity
                ?: throw IllegalStateException("Request take picture has no activity")
        val permission = PermissionChecker.checkSelfPermission(activity, Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), MY_CAMERA_REQUEST_CODE)
        } else {
            try {
                photoFilePath = TakePicture.requestTakePicture(activity)?.absolutePath
                binder.ivAddPhoto.visibility = View.GONE
            } catch (ex: Exception) {
                showSnack(R.string.image_file_not_found)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("photoFilePath", photoFilePath)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            try {
                photoFilePath = TakePicture.requestTakePicture(activity)?.absolutePath
            } catch (ex: Exception) {
                showSnack(R.string.image_file_not_found)
            }
        }
    }

    override fun onPictureCaptured() {
        showPicture()
    }

    private fun showPicture() {
        val imageFilePath = photoFilePath ?: return

        // Compress bitmap
        val proportion = binder.ivPhoto.width.toFloat() / binder.ivPhoto.height.toFloat()
        ImageUtils.compressBitmapFile(activity?.baseContext, imageFilePath, proportion) {
            ImageLoader.setImageWithCircle(binder.ivPhoto, imageFilePath, onError = {
                showSnack(R.string.image_load_failed)
            })
        }
    }
}
