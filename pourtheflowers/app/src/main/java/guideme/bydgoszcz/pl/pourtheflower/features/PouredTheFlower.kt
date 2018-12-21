package guideme.bydgoszcz.pl.pourtheflower.features

import android.content.Context
import guideme.bydgoszcz.pl.pourtheflower.actions.SaveUserChanges
import guideme.bydgoszcz.pl.pourtheflower.actions.SetFlowerPoured
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import javax.inject.Inject

class PouredTheFlower @Inject constructor(private val context: Context, private val saveChanges: SaveUserChanges) {
    fun pour(item: UiItem, onFinished: () -> Unit) {
        SetFlowerPoured.set(context, item)
        saveChanges.save {
            // no op
        }
        onFinished()
    }
}