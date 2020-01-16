package pl.bydgoszcz.guideme.podlewacz.dagger

import dagger.Component
import pl.bydgoszcz.guideme.podlewacz.MainActivity
import pl.bydgoszcz.guideme.podlewacz.notifications.AlarmReceiver
import pl.bydgoszcz.guideme.podlewacz.notifications.DeviceBootReceiver
import pl.bydgoszcz.guideme.podlewacz.views.fragments.*
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
    fun inject(target: AlarmReceiver)
    fun inject(target: DeviceBootReceiver)
    fun inject(target: SettingsFragment)
}