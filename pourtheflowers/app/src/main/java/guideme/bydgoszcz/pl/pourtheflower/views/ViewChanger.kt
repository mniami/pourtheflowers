package guideme.bydgoszcz.pl.pourtheflower.views

import android.support.v4.app.Fragment
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem

interface ViewChanger {
    fun showItem(uiItem: UiItem)
    fun editItem(uiItem: UiItem)
    fun showUserItems()
    fun showAllItems()
    fun showNewItemAdd()
    fun getCurrentFragment(): Fragment?
}