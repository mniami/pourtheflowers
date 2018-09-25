package guideme.bydgoszcz.pl.pourtheflower.features

import guideme.bydgoszcz.pl.pourtheflower.actions.SaveUserChanges
import guideme.bydgoszcz.pl.pourtheflower.model.UserUiItem
import guideme.bydgoszcz.pl.pourtheflower.threads.runInBackground
import javax.inject.Inject

class UpdateUser @Inject constructor(private val saveUserChanges: SaveUserChanges) {
    fun save(userUi: UserUiItem, onFinished: () -> Unit) {
        runInBackground {
            //TODO
        }
    }
}