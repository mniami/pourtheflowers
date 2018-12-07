package guideme.bydgoszcz.pl.pourtheflower.loaders

import guideme.bydgoszcz.pl.pourtheflower.actions.LoadItemsFromResources
import guideme.bydgoszcz.pl.pourtheflower.actions.LoadUser
import guideme.bydgoszcz.pl.pourtheflower.actions.UpdateUserItems
import guideme.bydgoszcz.pl.pourtheflower.threads.runInBackground
import guideme.bydgoszcz.pl.pourtheflower.threads.runOnUi
import guideme.bydgoszcz.pl.pourtheflower.utils.asyncIO
import javax.inject.Inject

class DataLoader @Inject constructor(private val loadFlowersFromResources: LoadItemsFromResources,
                                     private val loadUser: LoadUser,
                                     private val updateUserFlowers: UpdateUserItems,
                                     private val imageLoader : ImageLoader) {
    fun load(onFinished: () -> Unit) {
        runInBackground {
            loadFlowersFromResources.load()
            loadUser.load()
            updateUserFlowers.update()

            runOnUi {
                onFinished()
            }
        }.onError {
            runOnUi { onFinished() }
        }
    }
}