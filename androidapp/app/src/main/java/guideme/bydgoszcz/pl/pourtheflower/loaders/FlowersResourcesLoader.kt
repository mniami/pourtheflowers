package guideme.bydgoszcz.pl.pourtheflower.loaders

import android.app.Application
import guideme.bydgoszcz.pl.pourtheflower.R
import guideme.bydgoszcz.pl.pourtheflower.model.Flower
import org.json.JSONObject
import javax.inject.Inject

class FlowersResourcesLoader @Inject constructor(private val application: Application) {
    fun load(): List<Flower> {
        val json = application.resources.openRawResource(R.raw.flowers)
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