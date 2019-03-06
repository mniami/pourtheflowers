package pl.bydgoszcz.guideme.podlewacz.dagger

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import pl.bydgoszcz.guideme.podlewacz.serialization.DataCache
import pl.bydgoszcz.guideme.podlewacz.utils.ContentProvider
import pl.bydgoszcz.guideme.podlewacz.utils.ContentProviderImpl
import java.util.*
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

    @Singleton
    @Provides
    fun provideRandom(): Random = Random()

    @Singleton
    @Provides
    fun provideContentProvider(): ContentProvider = ContentProviderImpl(application.baseContext)
}