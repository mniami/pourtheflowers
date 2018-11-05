package guideme.bydgoszcz.pl.pourtheflower.actions

import guideme.bydgoszcz.pl.pourtheflower.model.ItemsRepository
import javax.inject.Inject

class UpdateUserItems @Inject constructor(private val repo: ItemsRepository) {
    fun update() {
        val itemsStore = repo.itemsStore
        val user = repo.user

        itemsStore.forEach { allItems ->
            val item = user.items.firstOrNull { it.item.id == allItems.item.id }
            if (item != null) {
                item.isUser = true
                itemsStore[itemsStore.indexOf(allItems)] = item
            }
        }
    }
}