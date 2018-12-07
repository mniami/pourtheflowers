package guideme.bydgoszcz.pl.pourtheflower.views

import android.support.design.widget.FloatingActionButton
import android.support.v4.app.FragmentActivity
import guideme.bydgoszcz.pl.pourtheflower.R
import guideme.bydgoszcz.pl.pourtheflower.utils.getDrawableFromResources

class FabHelper(val activity: FragmentActivity?) {
    fun show(add: Boolean): FloatingActionButton? {
        val fab: FloatingActionButton = activity?.findViewById(R.id.fab) ?: return null
        val fabDrawableId = if (add) R.drawable.fab_add else R.drawable.fab_edit
        fab.setImageDrawable(activity.resources.getDrawableFromResources(fabDrawableId))
        fab.show()
        return fab
    }
    fun hide() {
        val fab: FloatingActionButton = activity?.findViewById(R.id.fab) ?: return
        fab.hide()
    }
}