package guideme.bydgoszcz.pl.pourtheflower.features

import guideme.bydgoszcz.pl.pourtheflower.actions.SaveUserChanges
import guideme.bydgoszcz.pl.pourtheflower.actions.UpdateUserItems
import guideme.bydgoszcz.pl.pourtheflower.model.*
import java.util.*
import javax.inject.Inject

class AddNewItem @Inject constructor(private val repo: ItemsRepository,
                                     private val updateUserItems: UpdateUserItems,
                                     private val saveChanges: SaveUserChanges,
                                     private val random: Random) {
    fun add(name: String, description: String, tags: List<Tag>, imageUrl: String, frequency : Int, onFinished: () -> Unit) {
        val item = Item(
                id = random.nextInt().toString(),
                name = name,
                description = description,
                tags = tags,
                imageUrl = imageUrl,
                frequency = frequency,
                notification = Notification(false, 0, 0))
        val uiItem = UiItem(item = item, isUser = true)
        repo.user.items.add(uiItem)

        updateUserItems.update()
        saveChanges.save(onFinished)
    }
}