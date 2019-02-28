package guideme.bydgoszcz.pl.pourtheflower.views.fragments.providers

import guideme.bydgoszcz.pl.pourtheflower.model.ItemsRepository
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import guideme.bydgoszcz.pl.pourtheflower.notifications.updateRemainingTime
import guideme.bydgoszcz.pl.pourtheflower.views.fragments.FlowerListFragment
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemsProvider @Inject constructor(private val repository: ItemsRepository) {
    fun getItems(listType: Int): List<UiItem> {
        val lib = repository.itemsStore
        val user = repository.user

        var flowers: List<UiItem> = when (listType) {
            FlowerListFragment.USER_LIST_TYPE -> user.items
            FlowerListFragment.ALL_LIST_TYPE -> lib
            else -> lib
        }
        flowers.forEach { it.updateRemainingTime() }
        return flowers.sortedBy {
            if (it.isUser && it.item.notification.enabled) {
                it.remainingTime.seconds
            } else {
                Int.MAX_VALUE
            }
        }.toList()
    }

}