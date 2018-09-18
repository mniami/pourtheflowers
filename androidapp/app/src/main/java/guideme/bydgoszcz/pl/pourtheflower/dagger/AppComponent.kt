package guideme.bydgoszcz.pl.pourtheflower.dagger

import dagger.Component
import guideme.bydgoszcz.pl.pourtheflower.FlowerListFragment
import javax.inject.Singleton


@Singleton
@Component(modules = [
    AppModule::class,
    FlowerModule::class//,
//    NetworkModule::class,
//    WikiModule::class
])
interface AppComponent {
    fun inject(target: FlowerListFragment)
}