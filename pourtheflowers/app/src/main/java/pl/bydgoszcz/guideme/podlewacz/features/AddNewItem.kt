package pl.bydgoszcz.guideme.podlewacz.features

import pl.bydgoszcz.guideme.podlewacz.model.*
import pl.bydgoszcz.guideme.podlewacz.utils.NotificationTime
import pl.bydgoszcz.guideme.podlewacz.views.fragments.actions.SaveItem
import pl.bydgoszcz.guideme.podlewacz.views.fragments.providers.ShortDesriptionProvider
import pl.bydgoszcz.guideme.podlewacz.views.model.UiItem
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