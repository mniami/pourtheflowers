package pl.bydgoszcz.guideme.podlewacz.views.model

import pl.bydgoszcz.guideme.podlewacz.model.Item
import pl.bydgoszcz.guideme.podlewacz.utils.NotificationTime
import java.io.Serializable

data class UiItem(val item: Item,
                  var isUser: Boolean,
                  var remainingTime: NotificationTime = NotificationTime.ZERO,
                  val shortDescription: String) : Serializable