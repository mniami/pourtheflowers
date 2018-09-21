package guideme.bydgoszcz.pl.pourtheflower.views.fragments

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import guideme.bydgoszcz.pl.pourtheflower.MainActivityHelper
import guideme.bydgoszcz.pl.pourtheflower.PourTheFlowerApplication
import guideme.bydgoszcz.pl.pourtheflower.R
import guideme.bydgoszcz.pl.pourtheflower.features.FlowersProvider
import guideme.bydgoszcz.pl.pourtheflower.model.FlowerUiItem
import guideme.bydgoszcz.pl.pourtheflower.utils.afterMeasured
import guideme.bydgoszcz.pl.pourtheflower.views.dialogs.ImageDialog
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_flower.*
import javax.inject.Inject

class FlowerFragment : Fragment() {
    private lateinit var flowerUiItem: FlowerUiItem
    @Inject
    lateinit var flowersProvider: FlowersProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        flowerUiItem = arguments?.getSerializable("Flower") as FlowerUiItem
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        return inflater.inflate(R.layout.fragment_flower, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity?.application as PourTheFlowerApplication).component.inject(this)

        val activity = activity
        if (activity is MainActivityHelper) {
            activity.showBackButton(true)
            activity.toolbar.title = flowerUiItem.flower.content
        }
        if (activity == null) {
            return
        }

        descriptionTextView.text = flowerUiItem.flower.description
        flowerImage.setOnClickListener {
            openImageFullScreen(flowerUiItem)
        }
        val fab = activity.findViewById<FloatingActionButton>(R.id.fab)
        fab?.setOnClickListener {
            val user = flowersProvider.getUser()
            user.flowers.add(flowerUiItem)
            flowersProvider.save(user) {
                Snackbar.make(view, "Dodano do Twojej listy", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                activity.supportFragmentManager?.popBackStack()
            }
        }
        loadImage(flowerUiItem)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                activity?.supportFragmentManager?.popBackStack()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun openImageFullScreen(flowerUiItem: FlowerUiItem) {
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
            putString(imageDialog.IMAGE_URL, flowerUiItem.flower.imageUrl)
        }
        imageDialog.show(transaction, "dialog")
    }

    private fun loadImage(flowerUiItem: FlowerUiItem) {
        val parentView = flowerImage.parent as ViewGroup
        parentView.afterMeasured {
            if (flowerUiItem.flower.imageUrl.isEmpty()) {
                return@afterMeasured
            }
            Picasso.get().load(flowerUiItem.flower.imageUrl)
                    .resize(parentView.measuredWidth, parentView.measuredHeight)
                    .centerInside()
                    .into(flowerImage)
        }
    }

    companion object {
        fun create(flower: FlowerUiItem): FlowerFragment {
            val fragment = FlowerFragment()
            val bundle = Bundle()
            bundle.putSerializable("Flower", flower)
            fragment.arguments = bundle
            return fragment
        }
    }
}
