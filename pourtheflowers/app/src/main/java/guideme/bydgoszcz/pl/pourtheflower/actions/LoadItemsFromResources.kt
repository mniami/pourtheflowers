package guideme.bydgoszcz.pl.pourtheflower.actions

import guideme.bydgoszcz.pl.pourtheflower.loaders.ItemsResourcesLoader
import guideme.bydgoszcz.pl.pourtheflower.mappers.ItemUiMapper
import guideme.bydgoszcz.pl.pourtheflower.model.ItemsRepository
import javax.inject.Inject

class LoadItemsFromResources @Inject constructor(private val repo: ItemsRepository,
                                                 private val resourcesLoader: ItemsResourcesLoader,
                                                 private val uiMapper: ItemUiMapper) {
    fun load() {
        if (!repo.isInitialized) {
            val unSerializedFlowers = resourcesLoader.load().sortedBy { it.content }
            repo.itemsStore = uiMapper.mapToUi(unSerializedFlowers)
            repo.isInitialized = true
        }
    }
}