package pl.bydgoszcz.guideme.podlewacz.views.fragments

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_flower_edit.*
import kotlinx.android.synthetic.main.tags.*
import pl.bydgoszcz.guideme.podlewacz.MainActivityHelper
import pl.bydgoszcz.guideme.podlewacz.R
import pl.bydgoszcz.guideme.podlewacz.features.AddNewItem
import pl.bydgoszcz.guideme.podlewacz.goBack
import pl.bydgoszcz.guideme.podlewacz.injector
import pl.bydgoszcz.guideme.podlewacz.model.Item
import pl.bydgoszcz.guideme.podlewacz.utils.NotificationTime
import pl.bydgoszcz.guideme.podlewacz.utils.setMenu
import pl.bydgoszcz.guideme.podlewacz.views.FabHelper
import pl.bydgoszcz.guideme.podlewacz.views.fragments.binders.EditDetailsFragmentBinder
import pl.bydgoszcz.guideme.podlewacz.views.model.UiItem
import javax.inject.Inject

class NewItemFragment : PictureFragment(), BackButtonHandler {

    @Inject
    lateinit var addNewItem: AddNewItem

    private lateinit var uiItem: UiItem
    private lateinit var binder: EditDetailsFragmentBinder

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

        ivAddPhoto.visibility = View.VISIBLE

        val activity = activity
                ?: throw IllegalStateException("New item on view created has no activity")

        if (activity is MainActivityHelper) {
            activity.showBackButton(true)
            activity.toolbar.title = getString(R.string.new_flower)
        }
    }

    override fun onBack() {
        validate {
            saveItem {
                goBack()
            }
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
        val frequency = if (binder.notificationEnabled) NotificationTime.fromDays(binder.pourFrequencyInDays) else NotificationTime.ZERO

        addNewItem.add(
                binder.name.toString(),
                binder.descriptionPure,
                binder.tags,
                ImageLoader.getPhotoFilePath(photoFilePath, context),
                frequency,
                onSuccess)
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        setMenu(menu, menuInflater, R.menu.edit_item_menu)
    }
}