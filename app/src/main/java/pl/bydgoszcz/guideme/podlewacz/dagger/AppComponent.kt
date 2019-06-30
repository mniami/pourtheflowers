package pl.bydgoszcz.guideme.podlewacz.dagger

import dagger.Component
import pl.bydgoszcz.guideme.podlewacz.MainActivity
import pl.bydgoszcz.guideme.podlewacz.views.fragments.EditDetailsFragment
import pl.bydgoszcz.guideme.podlewacz.views.fragments.FlowerListFragment
import pl.bydgoszcz.guideme.podlewacz.views.fragments.ItemDetailsFragment
import pl.bydgoszcz.guideme.podlewacz.views.fragments.NewItemFragment
import pl.bydgoszcz.guideme.podlewacz.views.fragments.library.LibraryFragment
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
    fun inject(target: LibraryFragment)
}