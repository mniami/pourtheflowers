package guideme.bydgoszcz.pl.pourtheflower.views.fragments

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.*
import com.squareup.picasso.Picasso
import guideme.bydgoszcz.pl.pourtheflower.MainActivityHelper
import guideme.bydgoszcz.pl.pourtheflower.PourTheFlowerApplication
import guideme.bydgoszcz.pl.pourtheflower.R
import guideme.bydgoszcz.pl.pourtheflower.features.AddItemToUser
import guideme.bydgoszcz.pl.pourtheflower.features.RemoveItemFromUser
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import guideme.bydgoszcz.pl.pourtheflower.utils.afterMeasured
import guideme.bydgoszcz.pl.pourtheflower.views.dialogs.ImageDialog
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_flower.*
import javax.inject.Inject

class ItemDetailsFragment : Fragment() {
    private lateinit var uiItem: UiItem

    @Inject
    lateinit var addItemToUser: AddItemToUser
    @Inject
    lateinit var removeItemFromUser: RemoveItemFromUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        uiItem = arguments?.getSerializable(ITEM_PARAM_NAME) as UiItem
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_flower, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        if (uiItem.isUser) {
            inflater?.inflate(R.menu.menu_flower, menu)
        } else {
            super.onCreateOptionsMenu(menu, inflater)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity?.application as PourTheFlowerApplication).component.inject(this)

        val activity = activity
        if (activity is MainActivityHelper) {
            activity.showBackButton(true)
            activity.toolbar.title = uiItem.item.content
        }
        if (activity == null) {
            return
        }

        descriptionTextView.text = uiItem.item.description
        flowerImage.setOnClickListener {
            openImageFullScreen(uiItem)
        }
        val fab = activity.findViewById<FloatingActionButton>(R.id.fab)
        fab?.setOnClickListener {
            addItemToUser.add(uiItem) {
                Snackbar.make(view, "Dodano do Twojej listy", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                activity.supportFragmentManager?.popBackStack()
            }
        }
        loadImage(uiItem)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                activity?.supportFragmentManager?.popBackStack()
                return true
            }
            R.id.remove_item -> {
                removeItemFromUser.remove(uiItem) {
                    activity?.supportFragmentManager?.popBackStack()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun openImageFullScreen(uiItem: UiItem) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        val dialog = activity?.supportFragmentManager?.findFragmentByTag("dialog")
        if (transaction == null) {
            return
        }
        if (dialog != null) {
            transaction.remove(dialog)
        }
        transaction.addToBackStack(null)

        val imageDialog = ImageDialog()
        imageDialog.arguments = Bundle().apply {
            putString(imageDialog.IMAGE_URL, uiItem.item.imageUrl)
        }
        imageDialog.show(transaction, "dialog")
    }

    private fun loadImage(flowerUiItem: UiItem) {
        val parentView = flowerImage.parent as ViewGroup
        parentView.afterMeasured {
            if (flowerUiItem.item.imageUrl.isEmpty()) {
                return@afterMeasured
            }
            Picasso.get().load(flowerUiItem.item.imageUrl)
                    .resize(parentView.measuredWidth, parentView.measuredHeight)
                    .centerInside()
                    .into(flowerImage)
        }
    }

    companion object {
        private const val ITEM_PARAM_NAME = "Item"
        fun create(uiItem: UiItem): ItemDetailsFragment {
            val fragment = ItemDetailsFragment()
            val bundle = Bundle()
            bundle.putSerializable(ITEM_PARAM_NAME, uiItem)
            fragment.arguments = bundle
            return fragment
        }
    }
}
