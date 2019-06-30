package pl.bydgoszcz.guideme.podlewacz.views.fragments.adapters

import android.R
import android.content.Context
import android.widget.ArrayAdapter

object PourFrequencyAdapterFactory {
    fun create(context: Context): ArrayAdapter<Int> {
        val range = (1..30)
        val repeatDaysValues = range.map { it }.toTypedArray()
        return ArrayAdapter(context, R.layout.simple_list_item_1, repeatDaysValues)
    }
}