package pl.bydgoszcz.guideme.podlewacz.views.fragments.adapters

import android.content.Context
import android.widget.ArrayAdapter
import pl.bydgoszcz.guideme.podlewacz.R as PodlewaczR
import android.R

object PourFrequencyAdapterFactory {
    fun create(context: Context): ArrayAdapter<SpinnerItem> {
        val range = (1..30)
        val daysString = context.getString(PodlewaczR.string.days)
        val dayString = context.getString(PodlewaczR.string.day)
        val repeatDaysValues = range.map {
            SpinnerItem("%d %s".format(it, if (it == 1) dayString else daysString), it)
        }.toTypedArray()
        return ArrayAdapter(context, R.layout.simple_list_item_1, repeatDaysValues)
    }
}

class SpinnerItem (val name: String, val value:Int) {
    override fun toString(): String {
        return name
    }
}