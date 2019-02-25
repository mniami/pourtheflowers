package guideme.bydgoszcz.pl.pourtheflower.features

import android.support.design.widget.Snackbar
import android.view.View
import guideme.bydgoszcz.pl.pourtheflower.R
import guideme.bydgoszcz.pl.pourtheflower.actions.SaveUserChanges
import guideme.bydgoszcz.pl.pourtheflower.actions.SetFlowerPouredNotification
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import guideme.bydgoszcz.pl.pourtheflower.notifications.updateRemainingTime
import guideme.bydgoszcz.pl.pourtheflower.utils.ContentProvider
import guideme.bydgoszcz.pl.pourtheflower.views.fragments.SlideDownAnimator
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