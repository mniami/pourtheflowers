package guideme.bydgoszcz.pl.pourtheflower.features

import android.content.Context
import android.support.design.widget.Snackbar
import android.view.View
import guideme.bydgoszcz.pl.pourtheflower.R
import guideme.bydgoszcz.pl.pourtheflower.actions.SaveUserChanges
import guideme.bydgoszcz.pl.pourtheflower.actions.SetFlowerPoured
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import guideme.bydgoszcz.pl.pourtheflower.notifications.updateRemainingTime
import guideme.bydgoszcz.pl.pourtheflower.views.fragments.SlideDownAnimator
import javax.inject.Inject

class PouredTheFlower @Inject constructor(private val context: Context, private val saveChanges: SaveUserChanges) {
    fun pour(item: UiItem, view : View, onFinished: () -> Unit) {
        SetFlowerPoured.set(context, item)
        saveChanges.save {
            item.updateRemainingTime()
            val message = String.format(context.getString(R.string.flower_poured), item.remainingTime.toDays())
            SlideDownAnimator().animate(view)
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                    .show()
            onFinished()
        }
    }
}