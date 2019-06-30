package pl.bydgoszcz.guideme.podlewacz.views

import androidx.fragment.app.Fragment
import pl.bydgoszcz.guideme.podlewacz.views.model.UiItem

interface ViewChanger {
    fun showItem(uiItem: UiItem)
    fun editItem(uiItem: UiItem)
    fun showUserItems()
    fun showAllItems()
    fun showNewItemAdd()
    fun getCurrentFragment(): Fragment?
}