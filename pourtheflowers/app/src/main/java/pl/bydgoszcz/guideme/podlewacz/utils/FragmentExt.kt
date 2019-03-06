package pl.bydgoszcz.guideme.podlewacz.utils

import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuInflater

fun Fragment.setMenu(menu: Menu?, menuInflater: MenuInflater?, menuResourceId: Int): Menu? {
    menu?.clear()
    menuInflater?.inflate(menuResourceId, menu)
    return menu
}