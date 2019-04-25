package pl.bydgoszcz.guideme.podlewacz.actions

import android.content.Context
import pl.bydgoszcz.guideme.podlewacz.R
import pl.bydgoszcz.guideme.podlewacz.mappers.ItemUiMapper
import pl.bydgoszcz.guideme.podlewacz.model.Item
import pl.bydgoszcz.guideme.podlewacz.model.Notification
import pl.bydgoszcz.guideme.podlewacz.repositories.ItemsRepository
import pl.bydgoszcz.guideme.podlewacz.serialization.UserDataCache
import pl.bydgoszcz.guideme.podlewacz.utils.NotificationTime
import pl.bydgoszcz.guideme.podlewacz.views.model.UiItem
import java.lang.String
import javax.inject.Inject

class LoadUser @Inject constructor(private val repository: ItemsRepository,
                                   private val userDataCache: UserDataCache,
                                   private val itemUiMapper: ItemUiMapper,
                                   private val context: Context) {
    fun load() {
        val deSerializedUser = userDataCache.load()
        val userUi = itemUiMapper.mapUserToUi(deSerializedUser, true)
        if (userUi.items.size == 0) {
            val imageUrl = String.format("android.resource://%s/drawable/flower", context.getString(R.string.package_name))
            userUi.items.add(UiItem(Item(
                    id = "sample",
                    name = context.getString(R.string.sample_flower_name),
                    description = context.getString(R.string.sample_flower_description),
                    imageUrl = imageUrl,
                    notification = Notification(enabled = true, repeatInTime = NotificationTime.fromDays(1))),
                    isUser = true,
                    shortDescription = context.getString(R.string.sample_flower_description)))
        }
        userUi.items.sortBy { it.item.name }
        repository.user = userUi
    }
}