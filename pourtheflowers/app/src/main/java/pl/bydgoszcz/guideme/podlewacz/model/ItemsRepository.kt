package pl.bydgoszcz.guideme.podlewacz.model

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemsRepository @Inject constructor() {
    var isInitialized: Boolean = false
    var user: UserUiItem = UserUiItem(mutableListOf())
    var itemsStore: MutableList<UiItem> = mutableListOf()
}
