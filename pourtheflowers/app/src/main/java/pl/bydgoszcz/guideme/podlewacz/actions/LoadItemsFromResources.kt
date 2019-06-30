package pl.bydgoszcz.guideme.podlewacz.actions

import pl.bydgoszcz.guideme.podlewacz.library.GithubDataLoader
import pl.bydgoszcz.guideme.podlewacz.mappers.ItemUiMapper
import pl.bydgoszcz.guideme.podlewacz.repositories.ItemsRepository
import javax.inject.Inject

class LoadItemsFromResources @Inject constructor(private val repo: ItemsRepository,
                                                 private val resourcesLoader: GithubDataLoader,
                                                 private val uiMapper: ItemUiMapper) {
    fun load() {
        if (!repo.isStoreLoaded) {
            val unSerializedFlowers = resourcesLoader.load().sortedBy { it.name }
            repo.itemsStore = uiMapper.mapToUi(unSerializedFlowers, false)
            repo.isStoreLoaded = true
        }
    }
}