package guideme.bydgoszcz.pl.pourtheflower.actions

import guideme.bydgoszcz.pl.pourtheflower.model.ItemsRepository
import javax.inject.Inject

class ReplaceUserItems @Inject constructor(private val repo: ItemsRepository) {
    fun replace() {
        val lib = repo.lib
        val user = repo.user

        lib.forEach { allItems ->
            val item = user.items.firstOrNull { it.item.id == allItems.item.id }
            if (item != null) {
                item.isUser = true
                lib[lib.indexOf(allItems)] = item
            }
        }
    }
}