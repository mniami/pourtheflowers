package guideme.bydgoszcz.pl.pourtheflower.views.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import guideme.bydgoszcz.pl.pourtheflower.*
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import guideme.bydgoszcz.pl.pourtheflower.utils.NotificationTime
import guideme.bydgoszcz.pl.pourtheflower.utils.setMenu
import guideme.bydgoszcz.pl.pourtheflower.views.FabHelper
import guideme.bydgoszcz.pl.pourtheflower.views.fragments.actions.SaveItem
import guideme.bydgoszcz.pl.pourtheflower.views.fragments.binders.EditDetailsFragmentBinder
import guideme.bydgoszcz.pl.pourtheflower.views.fragments.providers.EditDetailsFragmentFactory
import kotlinx.android.synthetic.main.fragment_flower_edit.*
import javax.inject.Inject

class EditDetailsFragment : Fragment() {
    @Inject
    lateinit var saveItem: SaveItem
    private lateinit var uiItem: UiItem
    private lateinit var originalUiItem: UiItem
    private lateinit var binder: EditDetailsFragmentBinder

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
            description = uiItem.item.description
            notificationEnabled = uiItem.item.notification.enabled
            pourFrequencyVisible = notificationEnabled
            pourFrequencyInDays = uiItem.item.notification.repeatInTime.toDays()
            flowerImageUrl = uiItem.item.imageUrl
            onNotificationEnabled = {
                uiItem.item.notification.enabled = notificationEnabled
                pourFrequencyVisible = notificationEnabled
            }
        }
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
            showConfirmationDialog(R.string.dialog_title_confirm_save, R.string.dialog_message_cofirm_save,
                    onSuccess = {
                        validate {
                            saveItem {
                                goBack()
                            }
                        }
                    },
                    onFailure = {
                        goBack()
                    })
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
            description = binder.description
            notification.enabled = binder.notificationEnabled
            notification.repeatInTime = NotificationTime.fromDays(binder.pourFrequencyInDays)
        }
        saveItem.saveItem(uiItem, onSuccess)
    }
}