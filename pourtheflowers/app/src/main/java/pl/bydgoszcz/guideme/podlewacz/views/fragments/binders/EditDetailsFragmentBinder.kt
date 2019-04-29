package pl.bydgoszcz.guideme.podlewacz.views.fragments.binders

import android.content.Context
import android.text.Html
import android.text.Spanned
import android.view.View
import android.widget.*
import pl.bydgoszcz.guideme.podlewacz.views.fragments.ImageLoader
import pl.bydgoszcz.guideme.podlewacz.views.fragments.adapters.PourFrequencyAdapterFactory

class EditDetailsFragmentBinder(
        private val context: Context,
        private val etName: EditText,
        private val etDescription: EditText,
        private val spinnerPourFrequency: Spinner,
        private val turnNotificationSwitch: Switch,
        private val lbFrequency: TextView,
        private val ivImage: ImageView) {
    var name: String
        get() {
            return etName.text.toString()
        }
        set(value) {
            etName.setText(value)
        }
    var descriptionHtml: Spanned
        get() {
            return etDescription.text
        }
        set(value) {
            etDescription.setText(value)
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
            return lbFrequency.visibility == View.VISIBLE
        }
        set(value) {
            val visibility = if (value) View.VISIBLE else View.GONE
            lbFrequency.visibility = visibility
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

    fun bind(block: EditDetailsFragmentBinder.() -> Unit) {
        turnNotificationSwitch.setOnClickListener { onNotificationEnabled(this) }
        spinnerPourFrequency.adapter = PourFrequencyAdapterFactory.create(context)
        block(this)
    }
}