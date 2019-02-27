package guideme.bydgoszcz.pl.pourtheflower.views.fragments.adapters

import android.R
import android.content.Context
import android.widget.Adapter
import android.widget.ArrayAdapter

object PourFrequencyAdapterFactory {
    fun create(context: Context): Adapter {
        val range = (1..30)
        val repeatDaysValues = range.map { it }.toTypedArray()
        return ArrayAdapter<Int>(context, R.layout.simple_list_item_1, repeatDaysValues)
    }
}