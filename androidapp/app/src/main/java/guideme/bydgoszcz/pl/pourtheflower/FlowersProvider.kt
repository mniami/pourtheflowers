package guideme.bydgoszcz.pl.pourtheflower

import android.content.Context
import guideme.bydgoszcz.pl.pourtheflower.dummy.FlowersContent
import org.json.JSONObject

class FlowersProvider(val context: Context) {
    private var flowers: List<FlowersContent.FlowerItem> = listOf()

    val items: List<FlowersContent.FlowerItem>
        get() {
            return flowers
        }

    fun load() {
        val json = context.resources.openRawResource(R.raw.flowers)
                .bufferedReader().use { it.readText() }
        val jsonObj = JSONObject(json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1))
        val foodJson = jsonObj.getJSONArray("data")
        val newFlowersList = mutableListOf<FlowersContent.FlowerItem>()

        for (i in 0 until foodJson!!.length()) {
            val jsonItem = foodJson.getJSONObject(i)
            val id = jsonItem.getString("id")
            val name = jsonItem.getString("name")
            val description = jsonItem.getString("description")
            val frequency = jsonItem.getInt("frequency")
            val imageUrl = jsonItem.getString("imageUrl")

            newFlowersList.add(FlowersContent.FlowerItem(id, name, description, frequency, imageUrl))
        }
        flowers = newFlowersList
    }
}