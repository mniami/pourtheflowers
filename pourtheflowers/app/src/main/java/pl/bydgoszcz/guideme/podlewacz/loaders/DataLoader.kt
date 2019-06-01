package pl.bydgoszcz.guideme.podlewacz.loaders

import pl.bydgoszcz.guideme.podlewacz.actions.LoadUser
import pl.bydgoszcz.guideme.podlewacz.threads.runInBackground
import pl.bydgoszcz.guideme.podlewacz.threads.runOnUi
import javax.inject.Inject

class DataLoader @Inject constructor(private val loadUser: LoadUser) {
    fun load(onFinished: () -> Unit) {
        runInBackground {
            loadUser.load()

            runOnUi {
                onFinished()
            }
        }.onError {
            runOnUi { onFinished() }
        }
    }
}