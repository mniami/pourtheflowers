package pl.bydgoszcz.guideme.podlewacz.model

import pl.bydgoszcz.guideme.podlewacz.utils.NotificationTime
import java.io.Serializable

data class Item(val id: String = "",
                var name: String = "",
                var description: String = "",
                val imageUrl: String = "",
                val tags: List<Tag> = emptyList(),
                val frequency: Int = 0,
                val notification: Notification = Notification()) : Serializable {
    override fun toString(): String = name
}

data class UiItem(val item: Item,
                  var isUser: Boolean,
                  var remainingTime: NotificationTime = NotificationTime.ZERO,
                  val shortDescription: String) : Serializable

fun Item.notification(block: Notification.() -> Unit) {
    notification.apply(block)
}