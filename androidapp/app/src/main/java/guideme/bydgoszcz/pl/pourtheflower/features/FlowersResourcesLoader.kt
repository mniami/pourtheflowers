package guideme.bydgoszcz.pl.pourtheflower.features

import android.content.Context
import guideme.bydgoszcz.pl.pourtheflower.R
import guideme.bydgoszcz.pl.pourtheflower.model.Flower
import org.json.JSONObject

class FlowersResourcesLoader {
    fun load(context: Context): List<Flower> {
        val json = context.resources.openRawResource(R.raw.flowers)
                .bufferedReader().use { it.readText() }
        val jsonObj = JSONObject(json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1))
        val foodJson = jsonObj.getJSONArray("data")
        val newFlowersList = mutableListOf<Flower>()

        for (i in 0 until foodJson!!.length()) {
            val jsonItem = foodJson.getJSONObject(i)
            val id = jsonItem.getString("id")
            val name = jsonItem.getString("name")
            val description = jsonItem.getString("description")
            val frequency = jsonItem.getInt("frequency")
            val imageUrl = jsonItem.getString("imageUrl")

            newFlowersList.add(Flower(id, name, description, frequency, imageUrl))
        }
        return newFlowersList
    }
}