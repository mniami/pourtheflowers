package pl.bydgoszcz.guideme.podlewacz.features

import android.support.design.widget.Snackbar
import android.view.View
import pl.bydgoszcz.guideme.podlewacz.R
import pl.bydgoszcz.guideme.podlewacz.actions.SaveUserChanges
import pl.bydgoszcz.guideme.podlewacz.actions.SetFlowerPouredNotification
import pl.bydgoszcz.guideme.podlewacz.views.model.UiItem
import pl.bydgoszcz.guideme.podlewacz.notifications.updateRemainingTime
import pl.bydgoszcz.guideme.podlewacz.utils.ContentProvider
import pl.bydgoszcz.guideme.podlewacz.views.fragments.SlideDownAnimator
import javax.inject.Inject

class PouredTheFlower @Inject constructor(private val contentProvider: ContentProvider,
                                          private val saveChanges: SaveUserChanges,
                                          private val setFlowerPouredNotification: SetFlowerPouredNotification) {
    fun pour(item: UiItem, view: View, onFinished: () -> Unit) {
        setFlowerPouredNotification.setUp(item)
        saveChanges.save {
            item.updateRemainingTime()
            val message = String.format(contentProvider.getString(R.string.flower_poured), item.remainingTime.toDays())
            SlideDownAnimator().animate(view)
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                    .show()
            onFinished()
        }
    }
}