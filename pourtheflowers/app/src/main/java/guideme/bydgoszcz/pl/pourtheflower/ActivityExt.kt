package guideme.bydgoszcz.pl.pourtheflower

import android.support.v4.app.Fragment
import guideme.bydgoszcz.pl.pourtheflower.dagger.AppComponent

inline fun <reified T : Fragment> T.injector(d: AppComponent.() -> Unit) {
    val component = (activity?.application as PourTheFlowerApplication).component
    component.apply(d)
}