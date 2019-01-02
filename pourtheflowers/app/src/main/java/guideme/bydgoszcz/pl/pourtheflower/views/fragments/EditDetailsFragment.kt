package guideme.bydgoszcz.pl.pourtheflower.views.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.ArrayAdapter
import guideme.bydgoszcz.pl.pourtheflower.R
import guideme.bydgoszcz.pl.pourtheflower.actions.SaveUserChanges
import guideme.bydgoszcz.pl.pourtheflower.actions.SetFlowerPoured
import guideme.bydgoszcz.pl.pourtheflower.goBack
import guideme.bydgoszcz.pl.pourtheflower.injector
import guideme.bydgoszcz.pl.pourtheflower.model.ItemsRepository
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import guideme.bydgoszcz.pl.pourtheflower.notifications.ItemsNotifications
import guideme.bydgoszcz.pl.pourtheflower.utils.NotificationTime
import guideme.bydgoszcz.pl.pourtheflower.utils.SystemTime
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

        FabHelper(activity).show(FabHelper.Option.SAVE)?.setOnClickListener {
            saveItem()
        }

        etName?.setText(uiItem.item.name)
        etDescription?.setText(uiItem.item.description)

        turnNotificationSwitch.isChecked = uiItem.item.notification.enabled
        turnNotificationSwitch.setOnClickListener {
            uiItem.item.notification.enabled = turnNotificationSwitch.isChecked
            val visibility = if (turnNotificationSwitch.isChecked) View.VISIBLE else View.GONE
            frequencySpinner.visibility = visibility
            tvFrequencyLabel.visibility = visibility

            if (turnNotificationSwitch.isChecked && frequencySpinner.adapter == null) {
                initFrequencySpinner()
            }
        }

        // setup frequency spinner
        if (uiItem.item.notification.enabled) {
            initFrequencySpinner()
        }

        val visibility = if (turnNotificationSwitch.isChecked) View.VISIBLE else View.GONE

        tvFrequencyLabel.visibility = visibility
        frequencySpinner.visibility = visibility

        imageLoader.load(uiItem)
    }

    private fun initFrequencySpinner() {
        val range = (1..30)
        val repeatDaysValues = range.map { it }.toTypedArray()
        val selectedValue = uiItem.item.notification.repeatInTime.toDays()

        frequencySpinner.adapter = ArrayAdapter<Int>(context, android.R.layout.simple_list_item_1, repeatDaysValues)
        if (selectedValue in range) {
            frequencySpinner.setSelection(selectedValue - 1)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, menuInflater: MenuInflater?) {
        setMenu(menu, menuInflater, R.menu.edit_item_menu)
    }

    private fun saveItem(): Boolean {
        val activity = activity ?: return false

        with(uiItem.item) {
            if (frequencySpinner.selectedItem != null) {
                notification.repeatInTime = NotificationTime.fromDays(frequencySpinner.selectedItem as Int)
            }
            with(uiItem.item) {
                if (notification.enabled) {
                    if (notification.lastNotificationTime.isZero()) {
                        SetFlowerPoured.set(activity, uiItem)
                    }
                } else {
                    notification.lastNotificationTime = SystemTime.ZERO
                }
            }
            name = etName.text.toString()
            description = etDescription.text.toString()

            repository.user.items.filter {
                it.item.id == id
            }.forEach {
                repository.user.items.remove(it)
            }
            repository.user.items.add(uiItem)
        }
        ItemsNotifications.setUpNotifications(activity, repository.user.items)
        saveUserChanges.save {
            activity.goBack()
        }
        return true
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