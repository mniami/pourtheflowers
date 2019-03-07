package pl.bydgoszcz.guideme.podlewacz.views.fragments

import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.math.MathUtils
import android.view.*
import pl.bydgoszcz.guideme.podlewacz.*
import pl.bydgoszcz.guideme.podlewacz.features.AddItemToUser
import pl.bydgoszcz.guideme.podlewacz.features.PouredTheFlower
import pl.bydgoszcz.guideme.podlewacz.features.RemoveItemFromUser
import pl.bydgoszcz.guideme.podlewacz.model.UiItem
import pl.bydgoszcz.guideme.podlewacz.notifications.updateRemainingTime
import pl.bydgoszcz.guideme.podlewacz.utils.getColorFromResource
import pl.bydgoszcz.guideme.podlewacz.utils.setMenu
import pl.bydgoszcz.guideme.podlewacz.views.FabHelper
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_flower.*
import pl.bydgoszcz.guideme.podlewacz.analytics.Analytics
import pl.bydgoszcz.guideme.podlewacz.analytics.BundleFactory
import javax.inject.Inject

class ItemDetailsFragment : Fragment() {
    private val analyticsName = "Item details"
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
    @Inject
    lateinit var analytics: Analytics

    private lateinit var uiItem: UiItem

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
        initItem()
        analytics.onViewCreated(BundleFactory.create()
                .putName(analyticsName)
                .putBoolean("NOTIFICATION_ENABLED", uiItem.item.notification.enabled)
                .putInt("NOTIFICATION_DAYS_AMOUNT", uiItem.item.notification.repeatInTime.toDays())
                .putBoolean("USER_ITEM", uiItem.isUser)
                .build())
    }

    private fun initItem() {
        val activity = activity ?: return
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
        when {
            remainingDays < 0 -> {
                header_layout.background.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY)
                remainingDaysTextView.background.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY)
                remainingDaysTextView.setTextColor(resources.getColorFromResource(R.color.brokenWhite))
                remainingDaysHeaderTextView.text = getString(R.string.flower_frequency_late_days_label_without_amount)
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
            remainingDays == 1 -> {
                remainingDaysTextView.background.colorFilter = null
                header_layout.background.colorFilter = null
                remainingDaysFooterTextView.setTextColor(resources.getColorFromResource(R.color.intenseLabelTextColor))
                remainingDaysHeaderTextView.setTextColor(resources.getColorFromResource(R.color.intenseLabelTextColor))
                remainingDaysTextView.text = remainingDays.toString()
                remainingDaysFooterTextView.text = getString(R.string.flower_frequency_in_day_footer)
                remainingDaysHeaderTextView.text = getString(R.string.flower_frequency_in_days_header)
            }
            else -> {
                remainingDaysTextView.background.colorFilter = null
                header_layout.background.colorFilter = null
                remainingDaysFooterTextView.setTextColor(resources.getColorFromResource(R.color.intenseLabelTextColor))
                remainingDaysHeaderTextView.setTextColor(resources.getColorFromResource(R.color.intenseLabelTextColor))
                remainingDaysTextView?.text = remainingDays.toString()
                remainingDaysHeaderTextView.text = getString(R.string.flower_frequency_in_days_header)
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
        ImageLoader.setImage(itemImage, uiItem.item.imageUrl)
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
            FlowerListFragment.changeListType(activity.supportFragmentManager, FlowerListFragment.USER_LIST_TYPE)
            if (uiItem.isUser) {
                viewChanger.editItem(uiItem)
            } else {
                addItemToUser.add(uiItem) {
                    showSnack(R.string.item_added_to_the_list_information, Snackbar.LENGTH_LONG)
                    initItem()
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
