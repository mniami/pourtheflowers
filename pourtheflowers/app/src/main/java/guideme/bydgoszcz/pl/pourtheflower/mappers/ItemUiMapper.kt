package guideme.bydgoszcz.pl.pourtheflower.mappers

import guideme.bydgoszcz.pl.pourtheflower.model.Item
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import guideme.bydgoszcz.pl.pourtheflower.model.User
import guideme.bydgoszcz.pl.pourtheflower.model.UserUiItem
import javax.inject.Inject

class ItemUiMapper @Inject constructor() {
    fun mapUserToUi(user: User): UserUiItem {
        return UserUiItem(mapToUi(user.items))
    }

    fun mapToUi(items: List<Item>): MutableList<UiItem> {
        return items.asSequence().map {
            UiItem(it, false)
        }.toMutableList()
    }

    fun mapUserUiToUser(userUi: UserUiItem): User {
        return User(userUi.items.map {
            it.item
        })
    }
}