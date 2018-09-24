package guideme.bydgoszcz.pl.pourtheflower.loaders

import guideme.bydgoszcz.pl.pourtheflower.actions.LoadFlowersFromResources
import guideme.bydgoszcz.pl.pourtheflower.actions.LoadUser
import guideme.bydgoszcz.pl.pourtheflower.actions.ReplaceUserFlowers
import guideme.bydgoszcz.pl.pourtheflower.threads.runInBackground
import guideme.bydgoszcz.pl.pourtheflower.threads.runOnUi
import javax.inject.Inject

class DataLoader @Inject constructor(private val loadFlowersFromResources: LoadFlowersFromResources,
                                     private val loadUser: LoadUser,
                                     private val replaceUserFlowers: ReplaceUserFlowers) {
    fun load(onFinished: () -> Unit) {
        runInBackground {
            loadFlowersFromResources.load()
            loadUser.load()
            replaceUserFlowers.replace()
            runOnUi {
                onFinished()
            }
        }.onError {
            runOnUi { onFinished() }
        }
    }
}