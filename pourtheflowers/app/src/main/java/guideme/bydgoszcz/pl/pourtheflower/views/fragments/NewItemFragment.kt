package guideme.bydgoszcz.pl.pourtheflower.views.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.checkSelfPermission
import android.view.*
import guideme.bydgoszcz.pl.pourtheflower.R
import guideme.bydgoszcz.pl.pourtheflower.features.AddNewItem
import guideme.bydgoszcz.pl.pourtheflower.injector
import guideme.bydgoszcz.pl.pourtheflower.views.TakePicture
import kotlinx.android.synthetic.main.new_item_layout.*
import java.io.File
import javax.inject.Inject

class NewItemFragment : Fragment(), TakingPictureThumbnail {

    @Inject
    lateinit var addNewItem: AddNewItem

    private var photoFilePath: String? = null

    companion object {
        const val MY_CAMERA_REQUEST_CODE = 100
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.new_item_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        injector { inject(this@NewItemFragment) }

        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            photoFilePath = savedInstanceState.getString("photoFilePath")
        }
        if (photoFilePath == null) {
            requestTakePicture()
        }
    }

    private fun saveItem() {
        val photoFilePath = photoFilePath
        if (photoFilePath == null) {
            Snackbar.make(view ?: return, getString(R.string.image_file_not_found), 2000)
            return
        }
        val imageUri = File(photoFilePath).toURI().toString()
        addNewItem.add(etName.text.toString(), etDescription.text.toString(), emptyList(), imageUri) {
            val activity = activity ?: return@add
            activity.supportFragmentManager.popBackStack()
        }
    }

    private fun requestTakePicture() {
        val activity = activity ?: return
        if (checkSelfPermission(activity, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), MY_CAMERA_REQUEST_CODE)
        } else {
            photoFilePath = TakePicture().requestTakePicture(activity)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if (photoFilePath != null) {
            outState.putString("photoFilePath", photoFilePath)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val activity = activity ?: return
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            TakePicture().requestTakePicture(activity)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, menuInflater: MenuInflater?) {
        menuInflater?.inflate(R.menu.edit_item_menu, menu)

        menu?.findItem(R.id.accept)?.setOnMenuItemClickListener {
            saveItem()
            true
        }
    }

    override fun onThumbnail(bitmap: Bitmap) {
        //addNewItem.add()
        imageView.setImageBitmap(bitmap)
    }

    override fun onPictureCaptured() {
        showPicture()
    }

    private fun showPicture() {
        val photoFilePath = photoFilePath ?: return

        TakePicture().setPictureToImageView(imageView, photoFilePath)
    }
}