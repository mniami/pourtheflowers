package guideme.bydgoszcz.pl.pourtheflower.views.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.checkSelfPermission
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import guideme.bydgoszcz.pl.pourtheflower.R
import guideme.bydgoszcz.pl.pourtheflower.TakingPicture
import kotlinx.android.synthetic.main.new_item_layout.*

class NewItemFragment : Fragment(), TakingPictureThumbnail {
    companion object {
        const val MY_CAMERA_REQUEST_CODE = 100
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity ?: return

        if (checkSelfPermission(activity, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), MY_CAMERA_REQUEST_CODE)
        } else {
            TakingPicture().requestTakePicture(activity)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val activity = activity ?: return
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            TakingPicture().requestTakePicture(activity)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.new_item_layout, container, false)
    }

    override fun onThumbnail(bitmap: Bitmap) {
        imageView.setImageBitmap(bitmap)
    }
}