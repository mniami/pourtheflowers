package pl.bydgoszcz.guideme.podlewacz

import androidx.multidex.MultiDexApplication
import pl.bydgoszcz.guideme.podlewacz.dagger.AppComponent
import pl.bydgoszcz.guideme.podlewacz.dagger.DaggerAppComponent
import pl.bydgoszcz.guideme.podlewacz.dagger.ItemModule

class PourTheFlowerApplication : MultiDexApplication() {
    lateinit var component: AppComponent
    override fun onCreate() {
        super.onCreate()
        component = initDagger(this)
    }

    private fun initDagger(app: PourTheFlowerApplication): AppComponent =
            DaggerAppComponent.builder()
                    .itemModule(ItemModule(app))
                    .build()
}