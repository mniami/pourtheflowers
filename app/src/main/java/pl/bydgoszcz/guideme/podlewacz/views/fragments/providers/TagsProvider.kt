package pl.bydgoszcz.guideme.podlewacz.views.fragments.providers

import pl.bydgoszcz.guideme.podlewacz.model.Tag
import pl.bydgoszcz.guideme.podlewacz.repositories.ItemsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TagsProvider @Inject constructor(private val repository: ItemsRepository) {
    fun getTags(): List<Tag> {
        return repository.user.items.flatMap {
            it.item.tags
        }
    }
}