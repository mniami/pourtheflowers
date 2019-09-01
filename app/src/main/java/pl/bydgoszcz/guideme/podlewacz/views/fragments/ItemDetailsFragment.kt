package pl.bydgoszcz.guideme.podlewacz.views.fragments

import android.os.Bundle
import android.text.Html
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_flower.*
import kotlinx.android.synthetic.main.tags.*
import pl.bydgoszcz.guideme.podlewacz.*
import pl.bydgoszcz.guideme.podlewacz.analytics.Analytics
import pl.bydgoszcz.guideme.podlewacz.analytics.BundleFactory
import pl.bydgoszcz.guideme.podlewacz.features.AddItemToUser
import pl.bydgoszcz.guideme.podlewacz.features.PouredTheFlower
import pl.bydgoszcz.guideme.podlewacz.features.RemoveItemFromUser
import pl.bydgoszcz.guideme.podlewacz.notifications.getRemainingDaysMessage
import pl.bydgoszcz.guideme.podlewacz.notifications.getRemainingNotificationTime
import pl.bydgoszcz.guideme.podlewacz.notifications.getRemainingSystemTime
import pl.bydgoszcz.guideme.podlewacz.notifications.updateRemainingTime
import pl.bydgoszcz.guideme.podlewacz.utils.SystemTime
import pl.bydgoszcz.guideme.podlewacz.utils.setMenu
import pl.bydgoszcz.guideme.podlewacz.utils.toVisibility
import pl.bydgoszcz.guideme.podlewacz.views.FabHelper
import pl.bydgoszcz.guideme.podlewacz.views.fragments.providers.ItemsProvider
import pl.bydgoszcz.guideme.podlewacz.views.model.UiItem
import javax.inject.Inject
import kotlin.math.abs

class ItemDetailsFragment : Fragment() {
    private val analyticsName = "Item details"
    private val viewChanger by lazy {
        (activity as MainActivityHelper).getViewChanger()
    }
    private val fullScreenImage by lazy {
        val a = activity ?: throw IllegalStateException("Item details, full screen has no activity")
        FullScreenImage(a)
    }

    @Inject
    lateinit var addItemToUser: AddItemToUser
    @Inject
    lateinit var removeItemFromUser: RemoveItemFromUser
    @Inject
    lateinit var itemsProvider: ItemsProvider
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val menuResource = if (uiItem.isUser) R.menu.menu_flower else R.menu.main
        setMenu(menu, inflater, menuResource)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injector { inject(this@ItemDetailsFragment) }

        uiItem = itemsProvider.getItem(uiItem.item.id)
        uiItem.updateRemainingTime()

        val activity = activity ?: throw IllegalStateException("Item details has no activity")

        if (activity is MainActivityHelper) {
            activity.showBackButton(true)
        }
        initItem()
        analytics.onViewCreated(BundleFactory.create()
                .putName(analyticsName)
                .putBoolean("NOTIFICATION_ENABLED", uiItem.item.notification.enabled)
                .putInt("NOTIFICATION_DAYS_AMOUNT", uiItem.item.notification.repeatInTime.toDays())
                .putBoolean("USER_ITEM", uiItem.isUser)
                .build())

    }

    private fun initItem() {
        val activity = activity
                ?: throw IllegalStateException("Item details - init item has no activity")

        activity.toolbar.title = uiItem.item.name
        tvDescription?.visibility = uiItem.item.description.isNotEmpty().toVisibility()
        tvDescription?.text = Html.fromHtml(uiItem.item.description)
        cvNotification?.visibility = uiItem.item.notification.enabled.toVisibility()
        setTags(cgTags, uiItem.item.tags, false)

        if (uiItem.item.notification.enabled) {
            val remainingNotification = uiItem.item.notification.getRemainingNotificationTime(SystemTime.current())
            val remainingSystem = uiItem.item.notification.getRemainingSystemTime()
            val timePassed = remainingNotification.value < 0
            val timeRemains = remainingNotification.value > 0
            val timePassedToday = remainingSystem.isToday()
            val timePassedButNotToday = timePassed && !timePassedToday
            val timePassedButNotTodayVisibility = timePassedButNotToday.toVisibility()

            tvNotificationDate?.text = remainingSystem.getDate()
            tvNotificationTime?.text = remainingSystem.getTime()
            tvRemainingTime.text = uiItem.item.notification.getRemainingDaysMessage(activity)

            if (timePassedButNotToday) {
                tvRemainingTime.setTextColor(resources.getColor(R.color.red))
            } else {
                tvRemainingTime.setTextColor(resources.getColor(R.color.colorPrimary))
            }
            ivNotificationAlert.visibility = timePassedButNotTodayVisibility
            tvAlertMessage.visibility = timePassedButNotTodayVisibility
            tvNotificationRemainingLabel.visibility = timeRemains.toVisibility()
        } else {
            ivNotificationAlert.visibility = View.GONE
        }

        btnWater.visibility = (uiItem.isUser && uiItem.item.notification.enabled).toVisibility()
        btnWater.setOnClickListener {
            pouredTheFlower.pour(uiItem, btnWater) {
                initItem()
            }
        }

        initFabButton(activity)
        initImage()
    }

    private fun initImage() {
        itemImage.visibility = uiItem.item.imageUrl.isNotEmpty().toVisibility()
        guideline.visibility = uiItem.item.imageUrl.isNotEmpty().toVisibility()
        ImageLoader.setImage(itemImage, uiItem.item.imageUrl)
        itemImage.onClick {
            fullScreenImage.open(uiItem)
        }
    }

    private fun initFabButton(activity: FragmentActivity) {
        val option = when (uiItem.isUser) {
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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
