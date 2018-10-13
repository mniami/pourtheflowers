package guideme.bydgoszcz.pl.pourtheflower.views

import guideme.bydgoszcz.pl.pourtheflower.model.UiItem

interface ViewChanger {
    fun showItem(uiItem: UiItem)
    fun editItem(uiItem: UiItem)
    fun showUserItems()
    fun showAllItems()
}