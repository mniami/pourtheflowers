package guideme.bydgoszcz.pl.pourtheflower.utils

import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuInflater

fun Fragment.setMenu(menu: Menu?, menuInflater: MenuInflater?, menuResourceId: Int): Menu? {
    menu?.clear()
    menuInflater?.inflate(menuResourceId, menu)
    return menu
}

fun <T> Menu.findActionViewItem(itemResourceId: Int, callback: (T) -> Unit): Menu {
    val item = findItem(itemResourceId) ?: return this
    val actionViewItem = item.actionView as T? ?: return this
    callback(actionViewItem)
    return this
}