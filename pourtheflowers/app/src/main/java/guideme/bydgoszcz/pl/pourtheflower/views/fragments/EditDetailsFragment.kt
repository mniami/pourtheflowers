package guideme.bydgoszcz.pl.pourtheflower.views.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import guideme.bydgoszcz.pl.pourtheflower.R
import guideme.bydgoszcz.pl.pourtheflower.actions.SaveUserChanges
import guideme.bydgoszcz.pl.pourtheflower.goBack
import guideme.bydgoszcz.pl.pourtheflower.injector
import guideme.bydgoszcz.pl.pourtheflower.model.ItemsRepository
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import guideme.bydgoszcz.pl.pourtheflower.notifications.ItemsNotifications
import guideme.bydgoszcz.pl.pourtheflower.utils.setMenu
import guideme.bydgoszcz.pl.pourtheflower.views.FabHelper
import kotlinx.android.synthetic.main.fragment_flower_edit.*
import javax.inject.Inject

class EditDetailsFragment : Fragment() {
    private lateinit var uiItem: UiItem
    @Inject
    lateinit var repository: ItemsRepository
    @Inject
    lateinit var saveUserChanges: SaveUserChanges

    private val imageLoader by lazy {
        ImageLoader(itemImage)
    }

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        uiItem = arguments?.getSerializable(ITEM_PARAM_NAME) as UiItem
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

        etName?.setText(uiItem.item.name)
        etDescription?.setText(uiItem.item.description)

        turnNotificationSwitch.isChecked = uiItem.item.notification.enabled
        turnNotificationSwitch.setOnClickListener {
            uiItem.item.notification.enabled = turnNotificationSwitch.isChecked
        }

        // setup frequency spinner
        val repeatDaysValues = (1..30).map { "$it" }.toTypedArray()
        frequencySpinner.adapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, repeatDaysValues)
        frequencySpinner.setSelection(uiItem.item.notification.repeatDays)

        imageLoader.load(uiItem)

        frequencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                uiItem.item.notification.repeatDays = 1
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                uiItem.item.notification.repeatDays = position
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, menuInflater: MenuInflater?) {
        setMenu(menu, menuInflater, R.menu.edit_item_menu)
        menu?.findItem(R.id.accept)?.setOnMenuItemClickListener {
            val activity = activity ?: return@setOnMenuItemClickListener false

            uiItem.item.name = etName.text.toString()
            uiItem.item.description = etDescription.text.toString()

            repository.user.items.filter {
                it.item.id == uiItem.item.id
            }.forEach {
                repository.user.items.remove(it)
            }
            repository.user.items.add(uiItem)

            ItemsNotifications(activity, saveUserChanges).setUpNotifications(repository.user.items)
            saveUserChanges.save {
                activity.goBack()
            }
            true
        }
    }

    companion object {
        private const val ITEM_PARAM_NAME = "Item"

        fun create(uiItem: UiItem): EditDetailsFragment {
            val fragment = EditDetailsFragment()
            val bundle = Bundle()
            bundle.putSerializable(ITEM_PARAM_NAME, uiItem)
            fragment.arguments = bundle
            return fragment
        }
    }
}