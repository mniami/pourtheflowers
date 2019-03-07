package pl.bydgoszcz.guideme.podlewacz

import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import pl.bydgoszcz.guideme.podlewacz.model.UiItem
import pl.bydgoszcz.guideme.podlewacz.views.ViewChanger
import pl.bydgoszcz.guideme.podlewacz.views.fragments.FlowerListFragment
import pl.bydgoszcz.guideme.podlewacz.views.fragments.ItemDetailsFragment
import pl.bydgoszcz.guideme.podlewacz.views.fragments.NewItemFragment
import pl.bydgoszcz.guideme.podlewacz.views.fragments.providers.EditDetailsFragmentFactory

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
                    .replace(frameLayoutId, FlowerListFragment.newInstance(FlowerListFragment.LIBRARY_LIST_TYPE), FlowerListFragment.BACK_STACK_NAME)
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