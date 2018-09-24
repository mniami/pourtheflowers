package guideme.bydgoszcz.pl.pourtheflower.mappers

import guideme.bydgoszcz.pl.pourtheflower.model.Flower
import guideme.bydgoszcz.pl.pourtheflower.model.FlowerUiItem
import guideme.bydgoszcz.pl.pourtheflower.model.User
import guideme.bydgoszcz.pl.pourtheflower.model.UserUiItem
import javax.inject.Inject

class FlowerUiMapper @Inject constructor() {
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