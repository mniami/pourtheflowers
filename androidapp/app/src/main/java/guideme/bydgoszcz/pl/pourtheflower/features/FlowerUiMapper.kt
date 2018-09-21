package guideme.bydgoszcz.pl.pourtheflower.features

import guideme.bydgoszcz.pl.pourtheflower.model.Flower
import guideme.bydgoszcz.pl.pourtheflower.model.FlowerUiItem
import guideme.bydgoszcz.pl.pourtheflower.model.User
import guideme.bydgoszcz.pl.pourtheflower.model.UserUiItem

class FlowerUiMapper {
    fun mapUserToUi(user: User): UserUiItem {
        return UserUiItem(mapFlowersToUi(user.flowers))
    }

    fun mapFlowersToUi(flowers: List<Flower>): MutableList<FlowerUiItem> {
        return flowers.asSequence().map {
            FlowerUiItem(it, false)
        }.toMutableList()
    }

    fun mapUserUiToUser(userUi: UserUiItem): User {
        return User(userUi.flowers.map {
            it.flower
        })
    }
}