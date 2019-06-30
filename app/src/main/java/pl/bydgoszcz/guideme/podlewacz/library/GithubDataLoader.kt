package pl.bydgoszcz.guideme.podlewacz.library

import pl.bydgoszcz.guideme.podlewacz.model.Item
import pl.bydgoszcz.guideme.podlewacz.utils.readJson
import java.net.URL
import javax.inject.Inject

class GithubDataLoader @Inject constructor(){
    private val endpointUrl = "https://raw.githubusercontent.com/mniami/staticcontent/master/flowers.json"

    fun load(): List<Item> {
        val items = URL(endpointUrl).readJson<ItemList>()
        return items.data
    }
}