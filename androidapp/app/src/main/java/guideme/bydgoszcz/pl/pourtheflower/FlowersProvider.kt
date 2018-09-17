package guideme.bydgoszcz.pl.pourtheflower

import android.content.Context
import guideme.bydgoszcz.pl.pourtheflower.model.Flower
import guideme.bydgoszcz.pl.pourtheflower.threads.runInBackground
import guideme.bydgoszcz.pl.pourtheflower.threads.runOnUi
import org.json.JSONObject

class FlowersProvider(val context: Context) {
    fun load(listener: (List<Flower>) -> Unit) {
        runInBackground {
            val flowers = _load()
            runOnUi {
                listener.invoke(flowers)
            }
        }
    }

    private fun _load(): List<Flower> {
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