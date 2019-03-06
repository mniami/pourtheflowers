package pl.bydgoszcz.guideme.podlewacz.actions

import pl.bydgoszcz.guideme.podlewacz.loaders.ItemsResourcesLoader
import pl.bydgoszcz.guideme.podlewacz.mappers.ItemUiMapper
import pl.bydgoszcz.guideme.podlewacz.model.ItemsRepository
import javax.inject.Inject

class LoadItemsFromResources @Inject constructor(private val repo: ItemsRepository,
                                                 private val resourcesLoader: ItemsResourcesLoader,
                                                 private val uiMapper: ItemUiMapper) {
    fun load() {
        if (!repo.isInitialized) {
            val unSerializedFlowers = resourcesLoader.load().sortedBy { it.name }
            repo.itemsStore = uiMapper.mapToUi(unSerializedFlowers, false)
            repo.isInitialized = true
        }
    }
}