package pl.bydgoszcz.guideme.podlewacz.views.fragments

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import pl.bydgoszcz.guideme.podlewacz.views.dialogs.ImageDialog
import pl.bydgoszcz.guideme.podlewacz.views.model.UiItem

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