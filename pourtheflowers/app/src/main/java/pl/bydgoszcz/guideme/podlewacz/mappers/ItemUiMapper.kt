package pl.bydgoszcz.guideme.podlewacz.mappers

import pl.bydgoszcz.guideme.podlewacz.model.*
import javax.inject.Inject

class ItemUiMapper @Inject constructor() {
    fun mapUserToUi(user: User, isUser: Boolean): UserUiItem {
        return UserUiItem(mapToUi(user.items, isUser))
    }

    fun mapToUi(items: List<Item>, isUser: Boolean): MutableList<UiItem> {
        return items.asSequence().map {
            UiItem(it, isUser, shortDescription = ShortDesriptionProvider.provide(it.description))
        }.toMutableList()
    }

    fun mapUserUiToUser(userUi: UserUiItem): User {
        return User(userUi.items.map {
            it.item
        })
    }
}