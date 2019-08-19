package pl.bydgoszcz.guideme.podlewacz.views.fragments

import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import pl.bydgoszcz.guideme.podlewacz.model.Tag
import pl.bydgoszcz.guideme.podlewacz.utils.toVisibility

fun getTags(cgTags: ChipGroup) : List<Tag> {
    val result = mutableListOf<Tag>()
    for (i in 0 until cgTags.childCount) {
        val chip = cgTags.getChildAt(i) as Chip
        if (chip.isChecked && chip.tag != null)
            result.add(Tag(value = chip.tag as String))
    }
    return result
}

fun setTags(cgTags: ChipGroup, tags: List<Tag>, checkable: Boolean = true) {
    for (i in 0 until cgTags.childCount) {
        val chip = cgTags.getChildAt(i) as Chip
        chip.isChecked = tags.contains(Tag(value = chip.tag as String))
        if (checkable)
            chip.isCheckable = checkable
        else
            chip.visibility = chip.isChecked.toVisibility()
    }
}