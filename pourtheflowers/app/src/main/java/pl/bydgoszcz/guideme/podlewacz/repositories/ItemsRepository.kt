package pl.bydgoszcz.guideme.podlewacz.repositories

import pl.bydgoszcz.guideme.podlewacz.views.model.UiItem
import pl.bydgoszcz.guideme.podlewacz.views.model.UserUiItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemsRepository @Inject constructor() {
    var isStoreLoaded: Boolean = false
    var user: UserUiItem = UserUiItem(mutableListOf())
    var itemsStore: MutableList<UiItem> = mutableListOf()
}
