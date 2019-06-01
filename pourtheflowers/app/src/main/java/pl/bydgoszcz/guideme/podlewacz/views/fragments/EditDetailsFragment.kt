package pl.bydgoszcz.guideme.podlewacz.views.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_flower_edit.*
import pl.bydgoszcz.guideme.podlewacz.*
import pl.bydgoszcz.guideme.podlewacz.analytics.Analytics
import pl.bydgoszcz.guideme.podlewacz.utils.NotificationTime
import pl.bydgoszcz.guideme.podlewacz.utils.setMenu
import pl.bydgoszcz.guideme.podlewacz.views.FabHelper
import pl.bydgoszcz.guideme.podlewacz.views.fragments.actions.SaveItem
import pl.bydgoszcz.guideme.podlewacz.views.fragments.binders.EditDetailsFragmentBinder
import pl.bydgoszcz.guideme.podlewacz.views.fragments.providers.EditDetailsFragmentFactory
import pl.bydgoszcz.guideme.podlewacz.views.model.UiItem
import javax.inject.Inject

class EditDetailsFragment : Fragment() {
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
        binder = EditDetailsFragmentBinder(requireContext(), etName, etDescription, frequencySpinner, turnNotificationSwitch, tvFrequencyLabel, ivImage)
        binder.bind {
            name = uiItem.item.name
            descriptionPure = uiItem.item.description
            notificationEnabled = uiItem.item.notification.enabled
            pourFrequencyVisible = notificationEnabled
            pourFrequencyInDays = uiItem.item.notification.repeatInTime.toDays()
            flowerImageUrl = uiItem.item.imageUrl
            onNotificationEnabled = {
                uiItem.item.notification.enabled = notificationEnabled
                pourFrequencyVisible = notificationEnabled
            }
        }
        tvPhotoImage.visibility = View.GONE
        analytics.onViewCreated(analyticsName)
    }

    override fun onPause() {
        super.onPause()
        doOnBackPressed { true }
    }

    override fun onResume() {
        super.onResume()
        attachOnBackClicked()
    }

    override fun onCreateOptionsMenu(menu: Menu?, menuInflater: MenuInflater?) {
        setMenu(menu, menuInflater, R.menu.edit_item_menu)
    }

    private fun attachOnBackClicked() {
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
        if (binder.name.isEmpty()) {
            showSnack(R.string.name_cannot_be_empty_message)
            return
        }
        return onSuccess()
    }

    private fun saveItem(onSuccess: () -> Unit) {
        with(uiItem.item) {
            name = binder.name
            description = binder.descriptionPure
            notification.enabled = binder.notificationEnabled
            notification.repeatInTime = NotificationTime.fromDays(binder.pourFrequencyInDays)
        }
        saveItem.saveItem(uiItem, onSuccess)
    }
}