package guideme.bydgoszcz.pl.pourtheflower.dagger

import dagger.Component
import guideme.bydgoszcz.pl.pourtheflower.MainActivity
import guideme.bydgoszcz.pl.pourtheflower.views.fragments.EditDetailsFragment
import guideme.bydgoszcz.pl.pourtheflower.views.fragments.FlowerListFragment
import guideme.bydgoszcz.pl.pourtheflower.views.fragments.ItemDetailsFragment
import guideme.bydgoszcz.pl.pourtheflower.views.fragments.NewItemFragment
import javax.inject.Singleton


@Singleton
@Component(modules = [
    ItemModule::class
])
interface AppComponent {
    fun inject(target: MainActivity)
    fun inject(target: FlowerListFragment)
    fun inject(target: ItemDetailsFragment)
    fun inject(target: EditDetailsFragment)
    fun inject(target: NewItemFragment)
}