package guideme.bydgoszcz.pl.pourtheflower.model

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemsRepository @Inject constructor() {
    var isInitialized: Boolean = false
    var user: UserUiItem = UserUiItem(mutableListOf())
    var lib: MutableList<UiItem> = mutableListOf()
}
