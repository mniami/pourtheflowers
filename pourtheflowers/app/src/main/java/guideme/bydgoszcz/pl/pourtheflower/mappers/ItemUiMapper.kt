package guideme.bydgoszcz.pl.pourtheflower.mappers

import guideme.bydgoszcz.pl.pourtheflower.model.Item
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import guideme.bydgoszcz.pl.pourtheflower.model.User
import guideme.bydgoszcz.pl.pourtheflower.model.UserUiItem
import javax.inject.Inject

class ItemUiMapper @Inject constructor() {
    fun mapUserToUi(user: User, isUser: Boolean): UserUiItem {
        return UserUiItem(mapToUi(user.items, isUser))
    }

    fun mapToUi(items: List<Item>, isUser: Boolean): MutableList<UiItem> {
        return items.asSequence().map {
            UiItem(it, isUser)
        }.toMutableList()
    }

    fun mapUserUiToUser(userUi: UserUiItem): User {
        return User(userUi.items.map {
            it.item
        })
    }
}