package guideme.bydgoszcz.pl.pourtheflower.actions

import guideme.bydgoszcz.pl.pourtheflower.model.FlowersRepository
import javax.inject.Inject

class ReplaceUserFlowers @Inject constructor(private val repo: FlowersRepository) {
    fun replace() {
        val lib = repo.lib
        val user = repo.user

        lib.forEach { allFlower ->
            val flower = user.flowers.firstOrNull { it.flower.id == allFlower.flower.id }
            if (flower != null) {
                flower.isUser = true
                lib[lib.indexOf(allFlower)] = flower
            }
        }
    }
}