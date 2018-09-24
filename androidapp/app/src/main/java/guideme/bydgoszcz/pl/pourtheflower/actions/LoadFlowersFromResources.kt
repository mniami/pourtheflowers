package guideme.bydgoszcz.pl.pourtheflower.actions

import guideme.bydgoszcz.pl.pourtheflower.loaders.FlowersResourcesLoader
import guideme.bydgoszcz.pl.pourtheflower.mappers.FlowerUiMapper
import guideme.bydgoszcz.pl.pourtheflower.model.FlowersRepository
import javax.inject.Inject

class LoadFlowersFromResources @Inject constructor(private val repo: FlowersRepository,
                                                   private val resourcesLoader: FlowersResourcesLoader,
                                                   private val flowerUiMapper: FlowerUiMapper) {
    fun load() {
        if (!repo.isInitialized) {
            val unSerializedFlowers = resourcesLoader.load().sortedBy { it.content }
            repo.lib = flowerUiMapper.mapFlowersToUi(unSerializedFlowers)
            repo.isInitialized = true
        }
    }
}