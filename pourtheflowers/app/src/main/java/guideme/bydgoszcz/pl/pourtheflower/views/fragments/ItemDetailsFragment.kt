package guideme.bydgoszcz.pl.pourtheflower.views.fragments

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.*
import com.squareup.picasso.Picasso
import guideme.bydgoszcz.pl.pourtheflower.MainActivityHelper
import guideme.bydgoszcz.pl.pourtheflower.R
import guideme.bydgoszcz.pl.pourtheflower.features.AddItemToUser
import guideme.bydgoszcz.pl.pourtheflower.features.RemoveItemFromUser
import guideme.bydgoszcz.pl.pourtheflower.goBack
import guideme.bydgoszcz.pl.pourtheflower.injector
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import guideme.bydgoszcz.pl.pourtheflower.utils.afterMeasured
import guideme.bydgoszcz.pl.pourtheflower.utils.setMenu
import guideme.bydgoszcz.pl.pourtheflower.views.FabHelper
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_flower.*
import javax.inject.Inject

class ItemDetailsFragment : Fragment() {
    private lateinit var uiItem: UiItem

    private val viewChanger by lazy {
        (activity as MainActivityHelper).getViewChanger()
    }
    private val fullScreenImage by lazy {
        val a = activity ?: return@lazy null
        FullScreenImage(a)
    }

    @Inject
    lateinit var addItemToUser: AddItemToUser
    @Inject
    lateinit var removeItemFromUser: RemoveItemFromUser

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        uiItem = arguments?.getSerializable(ITEM_PARAM_NAME) as UiItem
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_flower, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        val menuResource = if (uiItem.isUser) R.menu.menu_flower else R.menu.main
        setMenu(menu, inflater, menuResource)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injector { inject(this@ItemDetailsFragment) }

        val activity = activity ?: return

        if (activity is MainActivityHelper) {
            activity.showBackButton(true)
            activity.toolbar.title = uiItem.item.name
            tvName.text = uiItem.item.name
        }
        descriptionTextView?.text = uiItem.item.description

        initFabButton()
        initImage()
    }

    private fun initImage() {
        val parentView = itemImage.parent as ViewGroup
        val file = uiItem.item.imageUrl

        parentView.afterMeasured {
            val width = parentView.width
            val height = parentView.height

            Picasso.get().load(file)
                    .resize(width, height)
                    .centerCrop()
                    .into(itemImage)
        }

        itemImage.setOnClickListener {
            fullScreenImage?.open(uiItem)
        }
    }

    private fun initFabButton() {
        FabHelper(activity).show(!uiItem.isUser)?.setOnClickListener {
            if (uiItem.isUser) {
                viewChanger.editItem(uiItem)
            } else {
                addItemToUser.add(uiItem) {
                    Snackbar.make(it, "Dodano do Twojej listy", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                    activity?.goBack()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                activity?.goBack()
                return true
            }
            R.id.remove_item -> {
                removeItemFromUser.remove(uiItem) {
                    activity?.goBack()
                }
            }
        }

        return super.onOptionsItemSelected(item)
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
