package guideme.bydgoszcz.pl.pourtheflower.loaders

import android.app.Application
import guideme.bydgoszcz.pl.pourtheflower.R
import guideme.bydgoszcz.pl.pourtheflower.model.Item
import org.json.JSONObject
import javax.inject.Inject

class ItemsResourcesLoader @Inject constructor(private val application: Application) {
    fun load(): List<Item> {
        val json = application.resources.openRawResource(R.raw.flowers)
                .bufferedReader().use { it.readText() }
        val jsonObj = JSONObject(json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1))
        val foodJson = jsonObj.getJSONArray("data")
        val items = mutableListOf<Item>()

        for (i in 0 until foodJson!!.length()) {
            val jsonItem = foodJson.getJSONObject(i)
            val id = jsonItem.getString("id")
            val name = jsonItem.getString("name")
            val description = jsonItem.getString("description")
            val imageUrl = jsonItem.getString("imageUrl")

            items.add(Item(id, name, description, imageUrl))
        }
        return items
    }
}