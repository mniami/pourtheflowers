package pl.bydgoszcz.guideme.podlewacz

import pl.bydgoszcz.guideme.podlewacz.views.ViewChanger

interface MainActivityHelper {
    fun showBackButton(showBackButton: Boolean)
    fun getViewChanger(): ViewChanger
}