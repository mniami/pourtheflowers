package pl.bydgoszcz.guideme.podlewacz.views.fragments

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.*
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_flower_edit.*
import kotlinx.android.synthetic.main.tags.*
import pl.bydgoszcz.guideme.podlewacz.*
import pl.bydgoszcz.guideme.podlewacz.analytics.Analytics
import pl.bydgoszcz.guideme.podlewacz.threads.runInBackground
import pl.bydgoszcz.guideme.podlewacz.utils.NotificationTime
import pl.bydgoszcz.guideme.podlewacz.utils.setMenu
import pl.bydgoszcz.guideme.podlewacz.views.FabHelper
import pl.bydgoszcz.guideme.podlewacz.views.fragments.actions.SaveItem
import pl.bydgoszcz.guideme.podlewacz.views.fragments.binders.EditDetailsFragmentBinder
import pl.bydgoszcz.guideme.podlewacz.views.fragments.providers.EditDetailsFragmentFactory
import pl.bydgoszcz.guideme.podlewacz.views.model.UiItem
import javax.inject.Inject

class EditDetailsFragment : PictureFragment(), BackButtonHandler {

    @Inject
    lateinit var saveItem: SaveItem
    @Inject
    lateinit var analytics: Analytics
    private lateinit var uiItem: UiItem
    private lateinit var originalUiItem: UiItem
    private lateinit var binder: EditDetailsFragmentBinder
    private val analyticsName = "Edit details"

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        originalUiItem = arguments?.getSerializable(EditDetailsFragmentFactory.ITEM_PARAM_NAME) as UiItem
        uiItem = originalUiItem.copy()
        photoFilePath = uiItem.item.imageUrl
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_flower_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        injector { inject(this@EditDetailsFragment) }

        super.onViewCreated(view, savedInstanceState)

        FabHelper(activity).hide()
        binder = EditDetailsFragmentBinder(requireContext(), etName, etDescription, frequencySpinner, turnNotificationSwitch, ivPhoto, cgTags)
        binder.bind {
            name = SpannableStringBuilder(uiItem.item.name)
            descriptionPure = uiItem.item.description
            notificationEnabled = uiItem.item.notification.enabled
            pourFrequencyVisible = notificationEnabled
            pourFrequencyInDays = uiItem.item.notification.repeatInTime.toDays()
            flowerImageUrl = uiItem.item.imageUrl
            tags = uiItem.item.tags
            onNotificationEnabled = {
                uiItem.item.notification.enabled = notificationEnabled
                pourFrequencyVisible = notificationEnabled
            }
        }
        // TODO: refactoring needed to add possibility to change picture
        // all the code is enclosed in NewItemFragment
        ivAddPhoto.visibility = View.GONE

        ivPhoto.setOnClickListener {
            requestTakePicture()
        }
        analytics.onViewCreated(analyticsName)
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        setMenu(menu, menuInflater, R.menu.edit_item_menu)
    }

    override fun onBack() {
        runInBackground {
            validate {
                saveItem {
                    goBack()
                }
            }
        }
    }

    private fun validate(onSuccess: () -> Unit) {
        if (binder.name.isNullOrEmpty()) {
            showSnack(R.string.name_cannot_be_empty_message)
            return
        }
        return onSuccess()
    }

    private fun saveItem(onSuccess: () -> Unit) {
        with(uiItem.item) {
            name = binder.name.toString()
            description = binder.descriptionPure
            notification.enabled = binder.notificationEnabled
            notification.repeatInTime = NotificationTime.fromDays(binder.pourFrequencyInDays)
            tags = binder.tags
            imageUrl = ImageLoader.getPhotoFilePath(photoFilePath, context)
        }
        saveItem.saveItem(uiItem, onSuccess)
    }
}