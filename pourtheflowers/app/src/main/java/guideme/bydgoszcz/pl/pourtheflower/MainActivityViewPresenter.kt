package guideme.bydgoszcz.pl.pourtheflower

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import guideme.bydgoszcz.pl.pourtheflower.views.ViewChanger
import guideme.bydgoszcz.pl.pourtheflower.views.fragments.EditDetailsFragment
import guideme.bydgoszcz.pl.pourtheflower.views.fragments.FlowerListFragment
import guideme.bydgoszcz.pl.pourtheflower.views.fragments.ItemDetailsFragment
import guideme.bydgoszcz.pl.pourtheflower.views.fragments.NewItemFragment

class MainActivityViewPresenter(private val supportFragmentManager: FragmentManager,
                                private val frameLayoutId: Int) : ViewChanger {
    private val itemListBackStackName = "itemList"
    private val itemBackStackName = "item"

    override fun showItem(uiItem: UiItem) {
        supportFragmentManager.beginTransaction()
                .replace(frameLayoutId, ItemDetailsFragment.create(uiItem), itemBackStackName)
                .addToBackStack(itemBackStackName)
                .commit()
    }

    override fun editItem(uiItem: UiItem) {
        supportFragmentManager.beginTransaction()
                .replace(frameLayoutId, EditDetailsFragment.create(uiItem), itemBackStackName)
                .addToBackStack(itemBackStackName)
                .commit()
    }

    override fun showUserItems() {
        supportFragmentManager.beginTransaction()
                .replace(frameLayoutId, FlowerListFragment.newInstance(FlowerListFragment.USER_LIST_TYPE), itemListBackStackName)
                .commit()
    }

    override fun showAllItems() {
        supportFragmentManager.beginTransaction()
                .replace(frameLayoutId, FlowerListFragment.newInstance(FlowerListFragment.ALL_LIST_TYPE), itemListBackStackName)
                .commit()
    }

    override fun showNewItemAdd() {
        supportFragmentManager.beginTransaction()
                .replace(frameLayoutId, NewItemFragment(), itemBackStackName)
                .commit()
    }

    override fun getCurrentFragment(): Fragment? {
        return supportFragmentManager.findFragmentByTag(itemBackStackName)
    }
}