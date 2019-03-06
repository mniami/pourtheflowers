package pl.bydgoszcz.guideme.podlewacz.views

import android.support.v4.app.Fragment
import pl.bydgoszcz.guideme.podlewacz.model.UiItem

interface ViewChanger {
    fun showItem(uiItem: UiItem)
    fun editItem(uiItem: UiItem)
    fun showUserItems()
    fun showAllItems()
    fun showNewItemAdd()
    fun getCurrentFragment(): Fragment?
}