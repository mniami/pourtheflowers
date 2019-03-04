package guideme.bydgoszcz.pl.pourtheflower

import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import guideme.bydgoszcz.pl.pourtheflower.views.ViewChanger
import guideme.bydgoszcz.pl.pourtheflower.views.fragments.FlowerListFragment
import guideme.bydgoszcz.pl.pourtheflower.views.fragments.ItemDetailsFragment
import guideme.bydgoszcz.pl.pourtheflower.views.fragments.NewItemFragment
import guideme.bydgoszcz.pl.pourtheflower.views.fragments.providers.EditDetailsFragmentFactory

class MainActivityViewPresenter(private val supportFragmentManager: FragmentManager,
                                private val frameLayoutId: Int) : ViewChanger {
    private val itemBackStackName = "item"
    private val handler = Handler()

    override fun showItem(uiItem: UiItem) {
        val info = "details" + uiItem.item.id
        handler.post {
            if (uiItem.isUser) {
                FlowerListFragment.changeListType(supportFragmentManager, FlowerListFragment.USER_LIST_TYPE)
            }
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            supportFragmentManager.beginTransaction()
                    .replace(frameLayoutId, ItemDetailsFragment.create(uiItem), info)
                    .addToBackStack(info)
                    .commit()
        }
    }

    override fun editItem(uiItem: UiItem) {
        val info = "edit" + uiItem.item.id
        handler.post {
            supportFragmentManager.beginTransaction()
                    .replace(frameLayoutId, EditDetailsFragmentFactory.create(uiItem), info)
                    .commit()
        }
    }

    override fun showUserItems() {
        handler.post {
            supportFragmentManager.beginTransaction()
                    .replace(frameLayoutId, FlowerListFragment.newInstance(FlowerListFragment.USER_LIST_TYPE), FlowerListFragment.BACK_STACK_NAME)
                    .commit()
        }
    }

    override fun showAllItems() {
        handler.post {
            supportFragmentManager.beginTransaction()
                    .replace(frameLayoutId, FlowerListFragment.newInstance(FlowerListFragment.ALL_LIST_TYPE), FlowerListFragment.BACK_STACK_NAME)
                    .commit()
        }
    }

    override fun showNewItemAdd() {
        handler.post {
            supportFragmentManager.beginTransaction()
                    .replace(frameLayoutId, NewItemFragment(), itemBackStackName)
                    .addToBackStack(itemBackStackName)
                    .commit()
        }
    }

    override fun getCurrentFragment(): Fragment? {
        return supportFragmentManager.findFragmentByTag(itemBackStackName)
    }
}