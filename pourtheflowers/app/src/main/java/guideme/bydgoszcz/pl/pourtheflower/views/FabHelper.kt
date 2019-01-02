package guideme.bydgoszcz.pl.pourtheflower.views

import android.content.res.ColorStateList
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.FragmentActivity
import guideme.bydgoszcz.pl.pourtheflower.R
import guideme.bydgoszcz.pl.pourtheflower.utils.getColorFromResource
import guideme.bydgoszcz.pl.pourtheflower.utils.getDrawableFromResources

class FabHelper(val activity: FragmentActivity?) {
    enum class Option {
        ADD,
        EDIT,
        SAVE
    }

    fun show(option: Option): FloatingActionButton? {
        val fab: FloatingActionButton = activity?.findViewById(R.id.fab) ?: return null
        val fabDrawableId = when (option) {
            Option.ADD -> R.drawable.fab_add
            Option.EDIT -> R.drawable.fab_edit
            Option.SAVE -> android.R.drawable.ic_menu_save
        }
        val backgroundColor = when(option){
            Option.ADD, Option.EDIT -> R.color.colorAccent
            Option.SAVE -> R.color.colorPrimary
        }

        fab.setImageDrawable(activity.resources.getDrawableFromResources(fabDrawableId))
        fab.backgroundTintList = ColorStateList.valueOf(activity.resources.getColorFromResource(backgroundColor))
        fab.hide()
        fab.show()
        return fab
    }

    fun hide() {
        val fab: FloatingActionButton = activity?.findViewById(R.id.fab) ?: return
        fab.hide()
    }
}