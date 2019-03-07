package pl.bydgoszcz.guideme.podlewacz.views.fragments.providers

import android.os.Bundle
import pl.bydgoszcz.guideme.podlewacz.views.model.UiItem
import pl.bydgoszcz.guideme.podlewacz.views.fragments.EditDetailsFragment

object EditDetailsFragmentFactory {
    const val ITEM_PARAM_NAME = "Item"

    fun create(uiItem: UiItem): EditDetailsFragment {
        val fragment = EditDetailsFragment()
        val bundle = Bundle()
        bundle.putSerializable(ITEM_PARAM_NAME, uiItem)
        fragment.arguments = bundle
        return fragment
    }
}