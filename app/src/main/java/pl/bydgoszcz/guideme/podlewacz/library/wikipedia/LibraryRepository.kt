package pl.bydgoszcz.guideme.podlewacz.library.wikipedia

import pl.bydgoszcz.guideme.podlewacz.model.Item
import javax.inject.Inject

class LibraryRepository @Inject constructor() {
    val items: List<Item> = emptyList()
}