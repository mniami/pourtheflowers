package pl.bydgoszcz.guideme.podlewacz.library

import pl.bydgoszcz.guideme.podlewacz.model.Item
import pl.bydgoszcz.guideme.podlewacz.model.Tag

data class ItemList(val data: List<Item>, val tags: List<Tag>)