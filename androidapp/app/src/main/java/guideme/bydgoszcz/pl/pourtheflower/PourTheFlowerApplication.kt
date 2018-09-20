package guideme.bydgoszcz.pl.pourtheflower

import android.app.Application
import guideme.bydgoszcz.pl.pourtheflower.dagger.AppComponent
import guideme.bydgoszcz.pl.pourtheflower.dagger.AppModule
import guideme.bydgoszcz.pl.pourtheflower.dagger.DaggerAppComponent
import guideme.bydgoszcz.pl.pourtheflower.dagger.FlowerModule

class PourTheFlowerApplication : Application() {
    lateinit var component: AppComponent
    override fun onCreate() {
        super.onCreate()
        component = initDagger(this)
    }

    private fun initDagger(app: PourTheFlowerApplication): AppComponent =
            DaggerAppComponent.builder()
                    .appModule(AppModule(app))
                    .flowerModule(FlowerModule(app))
                    .build()
}