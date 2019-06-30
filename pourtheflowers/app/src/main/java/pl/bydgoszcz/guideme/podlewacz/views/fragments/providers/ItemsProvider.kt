package pl.bydgoszcz.guideme.podlewacz.views.fragments.providers

import pl.bydgoszcz.guideme.podlewacz.notifications.updateRemainingTime
import pl.bydgoszcz.guideme.podlewacz.repositories.ItemsRepository
import pl.bydgoszcz.guideme.podlewacz.views.fragments.FlowerListFragment
import pl.bydgoszcz.guideme.podlewacz.views.model.UiItem
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
                it.remainingTime.value
            } else {
                Int.MAX_VALUE
            }
        }.toList()
    }

    fun getItem(id: String): UiItem {
        return repository.user.items.first { it.item.id == id }
    }

}