package guideme.bydgoszcz.pl.pourtheflower.dagger

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import guideme.bydgoszcz.pl.pourtheflower.serialization.DataCache
import javax.inject.Inject
import javax.inject.Singleton

@Module
class ItemModule @Inject constructor(private val application: Application) {

    @Singleton
    @Provides
    fun provideDataCache(): DataCache = DataCache(application.cacheDir.absolutePath)

    @Singleton
    @Provides
    fun provideContext(): Context = application

    @Singleton
    @Provides
    fun provideApplication(): Application = application


}