package pl.bydgoszcz.guideme.podlewacz.loaders

import android.app.Application
import pl.bydgoszcz.guideme.podlewacz.model.Item
import pl.bydgoszcz.guideme.podlewacz.model.Tag
import pl.bydgoszcz.guideme.podlewacz.utils.readJson
import java.net.URL
import javax.inject.Inject

class ItemsResourcesLoader @Inject constructor(private val application: Application) {
    private val endpointUrl = "https://raw.githubusercontent.com/mniami/staticcontent/master/flowers.json"

    fun load(): List<Item> {
        val items = URL(endpointUrl).readJson<ItemList>()
        return items.data
    }
}
data class ItemList (val data : List<Item>, val tags : List<Tag>)