package pl.bydgoszcz.guideme.podlewacz.views.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.*
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_flower_edit.*
import pl.bydgoszcz.guideme.podlewacz.*
import pl.bydgoszcz.guideme.podlewacz.features.AddNewItem
import pl.bydgoszcz.guideme.podlewacz.model.Item
import pl.bydgoszcz.guideme.podlewacz.utils.ImageUtils
import pl.bydgoszcz.guideme.podlewacz.utils.NotificationTime
import pl.bydgoszcz.guideme.podlewacz.utils.setMenu
import pl.bydgoszcz.guideme.podlewacz.views.FabHelper
import pl.bydgoszcz.guideme.podlewacz.views.TakePicture
import pl.bydgoszcz.guideme.podlewacz.views.fragments.binders.EditDetailsFragmentBinder
import pl.bydgoszcz.guideme.podlewacz.views.model.UiItem
import java.lang.String.format
import javax.inject.Inject

class NewItemFragment : Fragment(), TakingPictureThumbnail {
    @Inject
    lateinit var addNewItem: AddNewItem

    private lateinit var uiItem: UiItem
    private lateinit var binder: EditDetailsFragmentBinder

    private var photoFilePath: String? = null

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
            photoFilePath = savedInstanceState.getString("photoFilePath")
        }

        FabHelper(activity).hide()

        uiItem = UiItem(Item(), true, NotificationTime.ZERO, "")
        binder = EditDetailsFragmentBinder(requireContext(), etName, etDescription, frequencySpinner, turnNotificationSwitch, ivPhoto, cgTags)
        binder.bind {
            name = SpannableStringBuilder(uiItem.item.name)
            descriptionPure = uiItem.item.description
            notificationEnabled = uiItem.item.notification.enabled
            pourFrequencyVisible = notificationEnabled
            tags = uiItem.item.tags
            onNotificationEnabled = {
                uiItem.item.notification.enabled = notificationEnabled
                pourFrequencyVisible = notificationEnabled
            }
        }
        ivAddPhoto.setOnClickListener {
            requestTakePicture()
        }
        ivPhoto.setOnClickListener {
            requestTakePicture()
        }
        binder.notificationEnabled = true
        binder.pourFrequencyVisible = true
        val activity = activity
                ?: throw IllegalStateException("New item on view created has no activity")

        if (activity is MainActivityHelper) {
            activity.showBackButton(true)
            activity.toolbar.title = getString(R.string.new_flower)
        }
        registerSave()
    }

    private fun registerSave() {
        doOnBackPressed {
            validate {
                saveItem {
                    goBack()
                }
            }
            returnFalse()
        }
    }

    private fun validate(onSuccess: () -> Unit) {
        if (binder.name.isNullOrEmpty()) {
            goBack()
            return
        }
        return onSuccess()
    }

    private fun saveItem(onSuccess: () -> Unit) {
        val photoFilePath = photoFilePath
        val filePath = if (photoFilePath.isNullOrEmpty()) format("android.resource://%s/drawable/flower", getString(R.string.package_name)) else photoFilePath
        val frequency = if (binder.notificationEnabled) NotificationTime.fromDays(binder.pourFrequencyInDays) else NotificationTime.ZERO

        addNewItem.add(binder.name.toString(), binder.descriptionPure, binder.tags, filePath, frequency, onSuccess)
    }

    private fun requestTakePicture() {
        val activity = activity
                ?: throw IllegalStateException("Request take picture has no activity")

        if (checkSelfPermission(activity, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), MY_CAMERA_REQUEST_CODE)
        } else {
            try {
                photoFilePath = TakePicture.requestTakePicture(activity)?.absolutePath
                ivAddPhoto.visibility = View.GONE
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

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        setMenu(menu, menuInflater, R.menu.edit_item_menu)
    }

    override fun onPictureCaptured() {
        showPicture()
    }

    private fun showPicture() {
        val imageFilePath = photoFilePath ?: return

        // Compress bitmap
        val proportion = ivPhoto.width.toFloat() / ivPhoto.height.toFloat()
        ImageUtils.compressBitmapFile(activity?.baseContext, imageFilePath, proportion) {
            ImageLoader.setImage(ivPhoto, imageFilePath, onError = {
                showSnack(R.string.image_load_failed)
            })
        }
    }
}