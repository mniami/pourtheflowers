package guideme.bydgoszcz.pl.pourtheflower.views.fragments

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import guideme.bydgoszcz.pl.pourtheflower.views.dialogs.ImageDialog

class FullScreenImage(private val activity: FragmentActivity) {
    fun open(uiItem: UiItem) {
        val transaction = activity.supportFragmentManager?.beginTransaction()
        val dialog = activity.supportFragmentManager?.findFragmentByTag("dialog")
        if (transaction == null) {
            return
        }
        if (dialog != null) {
            transaction.remove(dialog)
        }
        transaction.addToBackStack(null)

        val imageDialog = ImageDialog()
        imageDialog.arguments = Bundle().apply {
            putString(ImageDialog.IMAGE_URL, uiItem.item.imageUrl)
        }
        imageDialog.show(transaction, "dialog")
    }
}