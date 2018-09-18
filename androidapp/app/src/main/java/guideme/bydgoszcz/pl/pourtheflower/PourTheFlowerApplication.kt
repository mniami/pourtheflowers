package guideme.bydgoszcz.pl.pourtheflower

import android.app.Application
import guideme.bydgoszcz.pl.pourtheflower.dagger.AppComponent

class PourTheFlowerApplication : Application() {

    lateinit var component: AppComponent

    //    private fun initDagger(app: PourTheFlowerApplication): AppComponent =
//            DaggerAppComponent.builder()
//                    .appModule(AppModule(app))
//                    .build()
}