package guideme.bydgoszcz.pl.pourtheflower

import android.support.v4.app.FragmentManager
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import guideme.bydgoszcz.pl.pourtheflower.views.fragments.FlowerListFragment
import guideme.bydgoszcz.pl.pourtheflower.views.fragments.ItemDetailsFragment

class MainActivityViewPresenter(private val supportFragmentManager: FragmentManager,
                                private val frameLayoutId: Int) {
    private val itemListBackStackName = "itemList"
    private val itemBackStackName = "item"

    fun showFlower(uiItem: UiItem) {
        supportFragmentManager.beginTransaction()
                .replace(frameLayoutId, ItemDetailsFragment.create(uiItem), itemBackStackName)
                .addToBackStack(itemBackStackName)
                .commit()
    }

    fun showUserItems() {
        supportFragmentManager.beginTransaction()
                .replace(frameLayoutId, FlowerListFragment.newInstance(FlowerListFragment.USER_LIST_TYPE), itemListBackStackName)
                .commit()
    }

    fun showAllItems() {
        supportFragmentManager.beginTransaction()
                .replace(frameLayoutId, FlowerListFragment.newInstance(FlowerListFragment.ALL_LIST_TYPE), itemListBackStackName)
                .commit()
    }
}