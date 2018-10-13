package guideme.bydgoszcz.pl.pourtheflower.dagger

import dagger.Component
import guideme.bydgoszcz.pl.pourtheflower.MainActivity
import guideme.bydgoszcz.pl.pourtheflower.views.fragments.EditDetailsFragment
import guideme.bydgoszcz.pl.pourtheflower.views.fragments.FlowerListFragment
import guideme.bydgoszcz.pl.pourtheflower.views.fragments.ItemDetailsFragment
import javax.inject.Singleton


@Singleton
@Component(modules = [
    AppModule::class,
    ItemModule::class//,
//    NetworkModule::class,
//    WikiModule::class
])
interface AppComponent {
    fun inject(target: MainActivity)
    fun inject(target: FlowerListFragment)
    fun inject(target: ItemDetailsFragment)
    fun inject(target: EditDetailsFragment)
}