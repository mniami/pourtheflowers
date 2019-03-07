package pl.bydgoszcz.guideme.podlewacz.views.fragments.providers

import pl.bydgoszcz.guideme.podlewacz.model.ItemsRepository
import pl.bydgoszcz.guideme.podlewacz.model.UiItem
import pl.bydgoszcz.guideme.podlewacz.notifications.updateRemainingTime
import pl.bydgoszcz.guideme.podlewacz.views.fragments.FlowerListFragment
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemsProvider @Inject constructor(private val repository: ItemsRepository) {
    fun getItems(listType: Int): List<UiItem> {
        val lib = repository.itemsStore
        val user = repository.user
        val flowers: List<UiItem> = when (listType) {
            FlowerListFragment.USER_LIST_TYPE -> user.items
            FlowerListFragment.LIBRARY_LIST_TYPE -> lib
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