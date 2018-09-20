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
import guideme.bydgoszcz.pl.pourtheflower.model.Flower
import guideme.bydgoszcz.pl.pourtheflower.utils.afterMeasured
import guideme.bydgoszcz.pl.pourtheflower.views.dialogs.ImageDialog
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_flower.*
import javax.inject.Inject

class FlowerFragment : Fragment() {
    private lateinit var flower: Flower
    @Inject
    lateinit var flowersProvider: FlowersProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        flower = arguments?.getSerializable("Flower") as Flower
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
            activity.toolbar.title = flower.content
        }
        if (activity == null) {
            return
        }

        descriptionTextView.text = flower.description
        flowerImage.setOnClickListener {
            openImageFullScreen(flower)
        }
        val fab = activity.findViewById<FloatingActionButton>(R.id.fab)
        fab?.setOnClickListener {
            val user = flowersProvider.getUser()
            user.flowers.add(flower)
            flowersProvider.save(user) {
                Snackbar.make(view, "Dodano do Twojej listy", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                activity.supportFragmentManager?.popBackStack()
            }
        }
        loadImage(flower)
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

    private fun openImageFullScreen(flower: Flower) {
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
            putString(imageDialog.IMAGE_URL, flower.imageUrl)
        }
        imageDialog.show(transaction, "dialog")
    }

    private fun loadImage(flower: Flower) {
        val parentView = flowerImage.parent as ViewGroup
        parentView.afterMeasured {
            if (flower.imageUrl.isEmpty()) {
                return@afterMeasured
            }
            Picasso.get().load(flower.imageUrl)
                    .resize(parentView.measuredWidth, parentView.measuredHeight)
                    .centerInside()
                    .into(flowerImage)
        }
    }

    companion object {
        fun create(flower: Flower): FlowerFragment {
            val fragment = FlowerFragment()
            val bundle = Bundle()
            bundle.putSerializable("Flower", flower)
            fragment.arguments = bundle
            return fragment
        }
    }
}
