package guideme.bydgoszcz.pl.pourtheflower.model

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FlowersRepository @Inject constructor() {
    var isInitialized: Boolean = false
    var user: UserUiItem = UserUiItem(mutableListOf())
    var lib: MutableList<FlowerUiItem> = mutableListOf()
}
