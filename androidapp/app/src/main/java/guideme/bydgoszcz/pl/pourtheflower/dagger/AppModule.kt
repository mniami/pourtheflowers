package guideme.bydgoszcz.pl.pourtheflower.dagger

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import guideme.bydgoszcz.pl.pourtheflower.features.FlowersProvider
import javax.inject.Singleton

@Module
class AppModule(private val app: Application) {
    @Provides
    @Singleton
    fun provideContext(): Context = app

    @Singleton
    fun provideFlowerProviders(): FlowersProvider = FlowersProvider(app)
}