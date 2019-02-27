package guideme.bydgoszcz.pl.pourtheflower.views.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import guideme.bydgoszcz.pl.pourtheflower.R
import guideme.bydgoszcz.pl.pourtheflower.goBack
import guideme.bydgoszcz.pl.pourtheflower.injector
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
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
    lateinit var uiItem: UiItem
    lateinit var binder: EditDetailsFragmentBinder


    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        uiItem = arguments?.getSerializable(EditDetailsFragmentFactory.ITEM_PARAM_NAME) as UiItem
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_flower_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injector { inject(this@EditDetailsFragment) }

        FabHelper(activity).hide()
        binder = EditDetailsFragmentBinder(requireContext(), etName, etDescription, frequencySpinner, turnNotificationSwitch, tvFrequencyLabel, ivImage)
        binder.bind {
            name = uiItem.item.name
            description = uiItem.item.description
            notificationEnabled = uiItem.item.notification.enabled
            pourFrequencyVisible = notificationEnabled
            flowerImageUrl = uiItem.item.imageUrl
            onNotificationEnabled = {
                uiItem.item.notification.enabled = notificationEnabled
                pourFrequencyVisible = notificationEnabled
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, menuInflater: MenuInflater?) {
        setMenu(menu, menuInflater, R.menu.edit_item_menu)
        menu?.findItem(R.id.accept)?.setOnMenuItemClickListener {
            saveItem()
        }
    }

    private fun saveItem(): Boolean {
        saveItem.saveItem(uiItem, binder.name, binder.description, binder.pourFrequencyInDays) {
            activity?.goBack()
        }
        return true
    }
}