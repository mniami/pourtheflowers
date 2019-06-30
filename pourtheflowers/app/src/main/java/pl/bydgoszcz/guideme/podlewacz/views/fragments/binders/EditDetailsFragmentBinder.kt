package pl.bydgoszcz.guideme.podlewacz.views.fragments.binders

import android.content.Context
import android.text.Editable
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Switch
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import pl.bydgoszcz.guideme.podlewacz.model.Tag
import pl.bydgoszcz.guideme.podlewacz.views.fragments.ImageLoader
import pl.bydgoszcz.guideme.podlewacz.views.fragments.adapters.PourFrequencyAdapterFactory

class EditDetailsFragmentBinder(
        private val context: Context,
        private val etName: TextInputEditText,
        private val etDescription: TextInputEditText,
        private val spinnerPourFrequency: Spinner,
        private val turnNotificationSwitch: Switch,
        private val ivImage: ImageView,
        private val cgTags: ChipGroup) {
    var name: Editable?
        get() {
            return etName.text
        }
        set(value) {
            etName.text = value
        }
    var descriptionHtml: Editable?
        get() {
            return etDescription.text
        }
        set(value) {
            etDescription.text = value
        }
    var descriptionPure: String
        get() {
            return etDescription.text.toString().replace("\n", "<br/>")
        }
        set(value) {
            etDescription.setText(Html.fromHtml(value))
        }
    var pourFrequencyInDays: Int
        get() {
            return if (spinnerPourFrequency.selectedItem != null) {
                spinnerPourFrequency.selectedItem as Int
            } else 0
        }
        set(value) {
            for (i in 0 until spinnerPourFrequency.adapter.count) {
                if (spinnerPourFrequency.adapter.getItem(i) == value) {
                    spinnerPourFrequency.setSelection(i)
                    return
                }
            }
        }
    var notificationEnabled: Boolean
        get() {
            return turnNotificationSwitch.isChecked
        }
        set(value) {
            turnNotificationSwitch.isChecked = value
        }
    var pourFrequencyVisible: Boolean
        get() {
            return spinnerPourFrequency.visibility == View.VISIBLE
        }
        set(value) {
            val visibility = if (value) View.VISIBLE else View.GONE
            spinnerPourFrequency.visibility = visibility
        }
    var onNotificationEnabled: EditDetailsFragmentBinder.() -> Unit = {}
    var flowerImageUrl: String
        get() {
            return ivImage.tag as String
        }
        set(value) {
            ivImage.tag = value
            ImageLoader.setImage(ivImage, value)
        }
    var tags: List<Tag>
        get() {
            val result = mutableListOf<Tag>()
            for (i in 0 until cgTags.childCount) {
                val chip = cgTags.getChildAt(i) as Chip
                if (chip.isChecked && chip.tag != null)
                    result.add(Tag(value = chip.tag as String))
            }
            return result
        }
        set(list) {
            for (i in 0 until cgTags.childCount) {
                val chip = cgTags.getChildAt(i) as Chip
                chip.isChecked = list.contains(Tag(value = chip.tag as String))
            }
        }

    fun bind(block: EditDetailsFragmentBinder.() -> Unit) {
        turnNotificationSwitch.setOnClickListener { onNotificationEnabled(this) }
        spinnerPourFrequency.adapter = PourFrequencyAdapterFactory.create(context)
        block(this)
    }
}