package guideme.bydgoszcz.pl.pourtheflower.views.fragments

import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.math.MathUtils
import android.view.*
import guideme.bydgoszcz.pl.pourtheflower.MainActivityHelper
import guideme.bydgoszcz.pl.pourtheflower.R
import guideme.bydgoszcz.pl.pourtheflower.features.AddItemToUser
import guideme.bydgoszcz.pl.pourtheflower.features.PouredTheFlower
import guideme.bydgoszcz.pl.pourtheflower.features.RemoveItemFromUser
import guideme.bydgoszcz.pl.pourtheflower.goBack
import guideme.bydgoszcz.pl.pourtheflower.injector
import guideme.bydgoszcz.pl.pourtheflower.model.RemainingDaysMessageProvider
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import guideme.bydgoszcz.pl.pourtheflower.notifications.updateRemainingTime
import guideme.bydgoszcz.pl.pourtheflower.utils.getColorFromResource
import guideme.bydgoszcz.pl.pourtheflower.utils.setMenu
import guideme.bydgoszcz.pl.pourtheflower.views.FabHelper
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_flower.*
import javax.inject.Inject

class ItemDetailsFragment : Fragment() {
    private lateinit var uiItem: UiItem
    private var animators : MutableList<ValueAnimator> = mutableListOf()

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
    @Inject
    lateinit var pouredTheFlower: PouredTheFlower

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

    override fun onPause() {
        super.onPause()
        animators.forEach {
            it.cancel()
        }
    }

    override fun onResume() {
        super.onResume()
        animators.forEach {
            it.start()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injector { inject(this@ItemDetailsFragment) }

        uiItem.updateRemainingTime()

        val activity = activity ?: return

        if (activity is MainActivityHelper) {
            activity.showBackButton(true)
            activity.toolbar.title = uiItem.item.name
            tvName.text = uiItem.item.name
        }
        descriptionTextView?.text = uiItem.item.description

        initRemainingBar()
        initFabButton(activity)
        initImage()
    }

    private fun initRemainingBar() {
        val visible = if (uiItem.item.notification.enabled) View.VISIBLE else View.GONE

        remainingDaysHeaderTextView.visibility = visible
        remainingDaysFooterTextView.visibility = visible
        remainingDaysTextView.visibility = visible
        header_layout.visibility = visible
        todayImageView.visibility = View.GONE

        if (!uiItem.item.notification.enabled) {
            return
        }

        val onClickListener = View.OnClickListener {
            pouredTheFlower.pour(uiItem, it) { }
        }

        val remainingDays = uiItem.remainingTime.toDays()
        remainingDaysHeaderTextView.text = RemainingDaysMessageProvider.provide(requireContext(), uiItem, false)

        when {
            remainingDays < 0 -> {
                header_layout.background.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY)
                remainingDaysTextView.background.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY)
                remainingDaysTextView.setTextColor(resources.getColorFromResource(R.color.brokenWhite))
                animateRemainingBar()
                remainingDaysHeaderTextView.setTextColor(resources.getColorFromResource(R.color.brokenWhite))
                remainingDaysFooterTextView.setTextColor(resources.getColorFromResource(R.color.brokenWhite))
                remainingDaysTextView?.text = (remainingDays * -1).toString()
            }
            remainingDays == 0 -> {
                remainingDaysTextView.background.colorFilter = null
                header_layout.background.colorFilter = null
                remainingDaysFooterTextView.text = getString(R.string.flower_frequency_today_label_without_amount_footer)
                remainingDaysHeaderTextView.text = getString(R.string.flower_frequency_today_label_without_amount_header)
                remainingDaysFooterTextView.setTextColor(resources.getColorFromResource(R.color.intenseLabelTextColor))
                remainingDaysHeaderTextView.setTextColor(resources.getColorFromResource(R.color.intenseLabelTextColor))
                todayImageView.visibility = View.VISIBLE
                remainingDaysTextView.text = ""
                todayImageView.setOnClickListener(onClickListener)
            }
            else -> {
                remainingDaysTextView.background.colorFilter = null
                header_layout.background.colorFilter = null
                remainingDaysFooterTextView.setTextColor(resources.getColorFromResource(R.color.intenseLabelTextColor))
                remainingDaysHeaderTextView.setTextColor(resources.getColorFromResource(R.color.intenseLabelTextColor))
                remainingDaysTextView?.text = remainingDays.toString()
            }
        }
        remainingDaysTextView.setOnClickListener(onClickListener)
    }

    private fun animateRemainingBar() {
        val min = 0f
        val max = 0.13f
        val middle = (max - min) / 2f

        animators.add(ValueAnimator.ofFloat(min, max)
                .apply {
                    duration = 1000
                    repeatCount = ValueAnimator.INFINITE
                    addUpdateListener {
                        val value = animatedValue as Float
                        var scale = if (value > middle) max - value else value
                        scale = MathUtils.clamp(scale, min, middle)
                        remainingDaysTextView.scaleY = 1f + scale
                        remainingDaysTextView.scaleX = 1f + scale
                    }
                })
    }

    private fun initImage() {
        ImageLoader.loadSimple(itemImage, uiItem.item.imageUrl)
        itemImage.setOnClickListener {
            fullScreenImage?.open(uiItem)
        }
    }

    private fun initFabButton(activity: FragmentActivity) {
        val option = when(uiItem.isUser){
            true -> FabHelper.Option.EDIT
            false -> FabHelper.Option.ADD
        }
        FabHelper(activity).show(option)?.setOnClickListener {
            if (uiItem.isUser) {
                viewChanger.editItem(uiItem)
            } else {
                addItemToUser.add(uiItem) {
                    Snackbar.make(it, getString(R.string.item_added_to_the_list_information), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                    (activity as MainActivityHelper).getViewChanger().showItem(uiItem)
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
