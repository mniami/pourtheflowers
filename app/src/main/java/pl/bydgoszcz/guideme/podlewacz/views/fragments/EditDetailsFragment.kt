package pl.bydgoszcz.guideme.podlewacz.views.fragments

import android.os.Bundle
import android.view.*
import pl.bydgoszcz.guideme.podlewacz.*
import pl.bydgoszcz.guideme.podlewacz.analytics.Analytics
import pl.bydgoszcz.guideme.podlewacz.databinding.FragmentFlowerEditBinding
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
    private lateinit var deprecatedCustomBinder: EditDetailsFragmentBinder
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
        _binder = FragmentFlowerEditBinding.inflate(inflater, container, false)
        return binder.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binder = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        injector { inject(this@EditDetailsFragment) }

        super.onViewCreated(view, savedInstanceState)

        FabHelper(activity).show(FabHelper.Option.SAVE)?.setOnClickListener {
            validate {
                saveItem {
                    goBack()
                }
            }
        }
        deprecatedCustomBinder = EditDetailsFragmentBinder(requireContext(),
            binder.etName, binder.textView2, binder.etDescription, binder.frequencySpinner, binder.turnNotificationSwitch, binder.ivPhoto, binder.tagsLayoutContainer.cgTags)
        deprecatedCustomBinder.bind {
            namePure = uiItem.item.name
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
        binder.ivAddPhoto.visibility = View.GONE

        binder.ivPhoto.setOnClickListener {
            requestTakePicture()
        }
        analytics.onViewCreated(analyticsName)
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        setMenu(menu, menuInflater, R.menu.edit_item_menu)
    }

    override fun onBack() {
        goBack()
    }

    private fun validate(onSuccess: () -> Unit) {
        if (deprecatedCustomBinder.namePure.isEmpty()) {
            showSnack(R.string.name_cannot_be_empty_message)
            return
        }
        return onSuccess()
    }

    private fun saveItem(onSuccess: () -> Unit) {
        with(uiItem.item) {
            name = deprecatedCustomBinder.namePure
            description = deprecatedCustomBinder.descriptionPure
            notification.enabled = deprecatedCustomBinder.notificationEnabled
            notification.repeatInTime = NotificationTime.fromDays(deprecatedCustomBinder.pourFrequencyInDays)
            tags = deprecatedCustomBinder.tags
            imageUrl = ImageLoader.getPhotoFilePath(photoFilePath, context)
        }
        saveItem.saveItem(uiItem, onSuccess)
    }
}
