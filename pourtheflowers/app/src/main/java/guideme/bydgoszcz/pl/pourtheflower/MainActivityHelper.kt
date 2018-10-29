package guideme.bydgoszcz.pl.pourtheflower

import guideme.bydgoszcz.pl.pourtheflower.views.ViewChanger

interface MainActivityHelper {
    fun showBackButton(showBackButton: Boolean)
    fun getViewChanger(): ViewChanger
}