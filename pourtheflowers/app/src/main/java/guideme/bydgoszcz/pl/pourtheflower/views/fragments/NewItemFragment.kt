package guideme.bydgoszcz.pl.pourtheflower.views.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.checkSelfPermission
import android.view.*
import guideme.bydgoszcz.pl.pourtheflower.MainActivityHelper
import guideme.bydgoszcz.pl.pourtheflower.R
import guideme.bydgoszcz.pl.pourtheflower.doOnBackStack
import guideme.bydgoszcz.pl.pourtheflower.features.AddNewItem
import guideme.bydgoszcz.pl.pourtheflower.injector
import guideme.bydgoszcz.pl.pourtheflower.model.Item
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import guideme.bydgoszcz.pl.pourtheflower.utils.NotificationTime
import guideme.bydgoszcz.pl.pourtheflower.utils.setMenu
import guideme.bydgoszcz.pl.pourtheflower.views.FabHelper
import guideme.bydgoszcz.pl.pourtheflower.views.TakePicture
import guideme.bydgoszcz.pl.pourtheflower.views.fragments.binders.EditDetailsFragmentBinder
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_flower_edit.*
import java.io.File
import javax.inject.Inject

class NewItemFragment : Fragment(), TakingPictureThumbnail {
    @Inject
    lateinit var addNewItem: AddNewItem
    lateinit var photoFilePath: File
    lateinit var uiItem: UiItem
    lateinit var binder: EditDetailsFragmentBinder

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
                    ?: throw IllegalArgumentException("missing photoFilePath")
        }
        FabHelper(activity).hide()
        uiItem = UiItem(Item(), true, NotificationTime.ZERO, "")
        binder = EditDetailsFragmentBinder(requireContext(), etName, etDescription, frequencySpinner, turnNotificationSwitch, tvFrequencyLabel, ivImage)
        binder.bind {
            name = uiItem.item.name
            description = uiItem.item.description
            notificationEnabled = uiItem.item.notification.enabled
            pourFrequencyVisible = notificationEnabled
            onNotificationEnabled = {
                uiItem.item.notification.enabled = notificationEnabled
                pourFrequencyVisible = notificationEnabled
            }
        }
        val activity = activity ?: return

        if (activity is MainActivityHelper) {
            activity.showBackButton(true)
            activity.toolbar.title = getString(R.string.new_flower)
        }
        doOnBackStack { saveItem() }
        requestTakePicture()
    }

    private fun saveItem(): Boolean {
        val imageUri = File(photoFilePath.absolutePath).toURI().toString()
        addNewItem.add(binder.name, binder.description, emptyList(), imageUri, NotificationTime.fromDays(binder.pourFrequencyInDays)) { }
        return true
    }

    private fun requestTakePicture() {
        if (checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), MY_CAMERA_REQUEST_CODE)
        } else {
            try {
                photoFilePath = TakePicture.requestTakePicture(requireActivity())
            } catch (ex: Exception) {
                Snackbar.make(view ?: return, getString(R.string.image_file_not_found), 2000)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("photoFilePath", photoFilePath)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            TakePicture.requestTakePicture(requireActivity())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, menuInflater: MenuInflater?) {
        setMenu(menu, menuInflater, R.menu.edit_item_menu)
    }

    override fun onThumbnail(bitmap: Bitmap) {
        ivImage.setImageBitmap(bitmap)
    }

    override fun onPictureCaptured() {
        showPicture()
    }

    private fun showPicture() {
        ImageLoader.setImage(ivImage, photoFilePath, ivImage.width, ivImage.height)
    }
}