package guideme.bydgoszcz.pl.pourtheflower.views.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.checkSelfPermission
import android.view.*
import android.widget.ArrayAdapter
import guideme.bydgoszcz.pl.pourtheflower.R
import guideme.bydgoszcz.pl.pourtheflower.features.AddNewItem
import guideme.bydgoszcz.pl.pourtheflower.injector
import guideme.bydgoszcz.pl.pourtheflower.utils.setMenu
import guideme.bydgoszcz.pl.pourtheflower.views.FabHelper
import guideme.bydgoszcz.pl.pourtheflower.views.TakePicture
import kotlinx.android.synthetic.main.fragment_flower_edit.*
import java.io.File
import javax.inject.Inject

class NewItemFragment : Fragment(), TakingPictureThumbnail {

    @Inject
    lateinit var addNewItem: AddNewItem

    private var photoFilePath: File? = null

    companion object {
        const val MY_CAMERA_REQUEST_CODE = 100
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_flower_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        injector { inject(this@NewItemFragment) }

        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            photoFilePath = savedInstanceState.getSerializable("photoFilePath") as File?
        }
        FabHelper(activity).show(FabHelper.Option.SAVE)?.setOnClickListener {
            saveItem()
        }
        if (photoFilePath == null) {
            requestTakePicture()
        }
        val repeatDaysValues = (1..30).map { it }.toTypedArray()
        frequencySpinner.adapter = ArrayAdapter<Int>(context, android.R.layout.simple_list_item_1, repeatDaysValues)
    }

    private fun saveItem() {
        val photoFilePath = photoFilePath
        if (photoFilePath == null) {
            Snackbar.make(view ?: return, getString(R.string.image_file_not_found), 2000)
            return
        }
        val imageUri = File(photoFilePath.absolutePath).toURI().toString()
        addNewItem.add(etName.text.toString(), etDescription.text.toString(), emptyList(), imageUri, frequencySpinner.selectedItem as Int) {
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
            outState.putSerializable("photoFilePath", photoFilePath)
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
        setMenu(menu, menuInflater, R.menu.edit_item_menu)
    }

    override fun onThumbnail(bitmap: Bitmap) {
        //addNewItem.add()
        itemImage.setImageBitmap(bitmap)
    }

    override fun onPictureCaptured() {
        showPicture()
    }

    private fun showPicture() {
        val photoFilePath = photoFilePath ?: return

        ImageLoader(itemImage).setImage(photoFilePath, itemImage.width, itemImage.height)
    }
}