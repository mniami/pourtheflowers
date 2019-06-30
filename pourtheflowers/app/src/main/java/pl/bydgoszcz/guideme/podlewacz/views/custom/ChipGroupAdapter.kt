package pl.bydgoszcz.guideme.podlewacz.views.custom

import android.content.Context
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import pl.bydgoszcz.guideme.podlewacz.model.Tag

class ChipGroupAdapter(private val context: Context, private val chipGroup: ChipGroup) {
    fun init(items: List<Tag>) {
        chipGroup.removeAllViews()
        for (item in items) {
            val chip = Chip(context)
            chip.text = item.value
            chipGroup.addView(chip)
        }
    }
}