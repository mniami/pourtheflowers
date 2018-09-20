package guideme.bydgoszcz.pl.pourtheflower.dagger

import android.app.Application
import dagger.Module
import dagger.Provides
import guideme.bydgoszcz.pl.pourtheflower.features.FlowersProvider
import guideme.bydgoszcz.pl.pourtheflower.serialization.DataCache
import javax.inject.Singleton

@Module
class FlowerModule(private val application: Application) {
    @Singleton
    @Provides
    fun provideFlowerProviders(dataCache: DataCache): FlowersProvider = FlowersProvider(application, dataCache)

    @Singleton
    @Provides
    fun provideDataCache(): DataCache = DataCache(application.cacheDir.absolutePath)
}