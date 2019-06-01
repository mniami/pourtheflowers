package pl.bydgoszcz.guideme.podlewacz.utils

import android.view.Menu
import android.view.MenuInflater
import androidx.fragment.app.Fragment

fun Fragment.setMenu(menu: Menu?, menuInflater: MenuInflater?, menuResourceId: Int): Menu? {
    menu?.clear()
    menuInflater?.inflate(menuResourceId, menu)
    return menu
}