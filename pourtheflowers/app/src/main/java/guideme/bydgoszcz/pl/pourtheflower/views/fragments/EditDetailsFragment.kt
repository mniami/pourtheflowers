package guideme.bydgoszcz.pl.pourtheflower.views.fragments

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.*
import android.widget.SeekBar
import guideme.bydgoszcz.pl.pourtheflower.PourTheFlowerApplication
import guideme.bydgoszcz.pl.pourtheflower.R
import guideme.bydgoszcz.pl.pourtheflower.actions.SaveUserChanges
import guideme.bydgoszcz.pl.pourtheflower.model.ItemsRepository
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import guideme.bydgoszcz.pl.pourtheflower.notifications.ItemsNotifications
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

    override fun onResume() {
        super.onResume()
        (activity?.application as PourTheFlowerApplication).component.inject(this)
        val fab: FloatingActionButton = activity?.findViewById(R.id.fab) ?: return
        fab.hide()

        etName?.setText(uiItem.item.content)
        etDescription?.setText(uiItem.item.description)

        turnNotificationSwitch.isChecked = uiItem.item.notification.enabled
        turnNotificationSwitch.setOnClickListener {
            uiItem.item.notification.enabled = turnNotificationSwitch.isChecked
        }
        seekBar.max = 30
        seekBar.progress = uiItem.item.notification.repeatDays
        imageLoader.load(uiItem)
        showFrequencyTime()

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                uiItem.item.notification.repeatDays = p1
                showFrequencyTime()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
    }

    private fun showFrequencyTime() {
        frequencyTextView.text = String.format(getString(R.string.frequency_day_label), uiItem.item.notification.repeatDays.toString())
    }

    override fun onPause() {
        super.onPause()
        val activity = activity ?: return

        repository.user.items.filter {
            it.item.id == uiItem.item.id
        }.forEach {
            repository.user.items.remove(it)
        }
        repository.user.items.add(uiItem)

        ItemsNotifications(activity).setUpNotifications(repository.user.items)
        saveUserChanges.save {
            // noop
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, menuInflater: MenuInflater?) {
        menuInflater?.inflate(R.menu.edit_item_menu, menu)

        menu?.findItem(R.id.accept)?.setOnMenuItemClickListener {
            activity?.supportFragmentManager?.popBackStack()
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