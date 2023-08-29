package pl.bydgoszcz.guideme.podlewacz.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import pl.bydgoszcz.guideme.podlewacz.MainActivityHelper
import pl.bydgoszcz.guideme.podlewacz.R
import pl.bydgoszcz.guideme.podlewacz.databinding.FragmentFlowerEditBinding
import pl.bydgoszcz.guideme.podlewacz.features.AddNewItem
import pl.bydgoszcz.guideme.podlewacz.goBack
import pl.bydgoszcz.guideme.podlewacz.injector
import pl.bydgoszcz.guideme.podlewacz.model.Item
import pl.bydgoszcz.guideme.podlewacz.utils.NotificationTime
import pl.bydgoszcz.guideme.podlewacz.utils.setMenu
import pl.bydgoszcz.guideme.podlewacz.views.FabHelper
import pl.bydgoszcz.guideme.podlewacz.views.IToolbarTitleContainer
import pl.bydgoszcz.guideme.podlewacz.views.fragments.binders.EditDetailsFragmentBinder
import pl.bydgoszcz.guideme.podlewacz.views.model.UiItem
import javax.inject.Inject

class NewItemFragment : PictureFragment(), BackButtonHandler {

    @Inject
    lateinit var addNewItem: AddNewItem

    private lateinit var uiItem: UiItem
    private lateinit var deprecatedCustomeBinder: EditDetailsFragmentBinder

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        Log.w(TAG, "Created view")
        _binder = FragmentFlowerEditBinding.inflate(inflater, container, false)
        return binder.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binder = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        injector { inject(this@NewItemFragment) }

        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            photoFilePath = savedInstanceState.getString("photoFilePath")
        }

        FabHelper(activity).hide()

        uiItem = UiItem(Item(), true, NotificationTime.ZERO, "")
        deprecatedCustomeBinder = EditDetailsFragmentBinder(
            requireContext(),
            binder.etName,
            binder.textView2,
            binder.etDescription,
            binder.frequencySpinner,
            binder.turnNotificationSwitch,
            binder.ivPhoto,
            binder.tagsLayoutContainer.cgTags)
        deprecatedCustomeBinder.bind {
            namePure = uiItem.item.name
            descriptionPure = uiItem.item.description
            notificationEnabled = uiItem.item.notification.enabled
            pourFrequencyVisible = notificationEnabled
            tags = uiItem.item.tags
            onNotificationEnabled = {
                uiItem.item.notification.enabled = notificationEnabled
                pourFrequencyVisible = notificationEnabled
            }
        }
        val act = activity ?: throw IllegalStateException("Flower list no activity")
        FabHelper(act).show(FabHelper.Option.SAVE)?.setOnClickListener {
            validate {
                saveItem {
                    goBack()
                }
            }
        }
        binder.ivAddPhoto.setOnClickListener {
            requestTakePicture()
        }
        binder.ivPhoto.setOnClickListener {
            requestTakePicture()
        }
        deprecatedCustomeBinder.notificationEnabled = true
        deprecatedCustomeBinder.pourFrequencyVisible = true

        binder.ivAddPhoto.visibility = View.VISIBLE

        val activity = activity
                ?: throw IllegalStateException("New item on view created has no activity")

        if (activity is MainActivityHelper) {
            activity.showBackButton(true)
        }
        if (activity is IToolbarTitleContainer){
            activity.toolbarTitle = getString(R.string.new_flower)
        }
    }

    override fun onBack() {
        goBack()
    }

    private fun validate(onSuccess: () -> Unit) {
        if (deprecatedCustomeBinder.namePure.isEmpty()) {
            goBack()
            return
        }
        return onSuccess()
    }

    private fun saveItem(onSuccess: () -> Unit) {
        val photoFilePath = photoFilePath
        val frequency = if (deprecatedCustomeBinder.notificationEnabled) NotificationTime.fromDays(deprecatedCustomeBinder.pourFrequencyInDays) else NotificationTime.ZERO

        addNewItem.add(
                deprecatedCustomeBinder.namePure,
                deprecatedCustomeBinder.descriptionPure,
                deprecatedCustomeBinder.tags,
                ImageLoader.getPhotoFilePath(photoFilePath, context),
                frequency,
                onSuccess)
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        setMenu(menu, menuInflater, R.menu.edit_item_menu)
    }
}
