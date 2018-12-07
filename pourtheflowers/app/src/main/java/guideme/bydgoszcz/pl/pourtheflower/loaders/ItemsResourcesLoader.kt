package guideme.bydgoszcz.pl.pourtheflower.loaders

import android.app.Application
import guideme.bydgoszcz.pl.pourtheflower.R
import guideme.bydgoszcz.pl.pourtheflower.model.Item
import guideme.bydgoszcz.pl.pourtheflower.model.Notification
import guideme.bydgoszcz.pl.pourtheflower.model.Tag
import guideme.bydgoszcz.pl.pourtheflower.utils.readJson
import org.json.JSONObject
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