package guideme.bydgoszcz.pl.pourtheflower.features

import guideme.bydgoszcz.pl.pourtheflower.model.*
import guideme.bydgoszcz.pl.pourtheflower.utils.NotificationTime
import guideme.bydgoszcz.pl.pourtheflower.views.fragments.actions.SaveItem
import java.util.*
import javax.inject.Inject

class AddNewItem @Inject constructor(private val saveItem: SaveItem,
                                     private val random: Random) {
    fun add(name: String, description: String, tags: List<Tag>, imageUrl: String, frequency: NotificationTime, onFinished: () -> Unit) {
        val item = Item(
                id = random.nextInt().toString(),
                name = name,
                description = description,
                tags = tags,
                imageUrl = imageUrl,
                notification = Notification(enabled = frequency.toMillis() > 0, repeatInTime = frequency))
        val uiItem = UiItem(item = item, isUser = true, shortDescription = ShortDesriptionProvider.provide(item.description))
        saveItem.saveItem(uiItem, onFinished)
    }
}