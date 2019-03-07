package pl.bydgoszcz.guideme.podlewacz.views.fragments.providers

import android.content.Context
import android.text.Html
import android.text.Spanned
import pl.bydgoszcz.guideme.podlewacz.R
import pl.bydgoszcz.guideme.podlewacz.views.model.UiItem

object RemainingDaysMessageProvider {
    fun provide(context: Context, item: UiItem): Spanned {
        val remainingDays = item.remainingTime.toDays()
        return Html.fromHtml(when {
            remainingDays == 0 -> {
                val messageId = R.string.flower_frequency_today_label
                context.getString(messageId)
            }
            remainingDays == -1 -> {
                val messageId = R.string.flower_frequency_late_yesterday_label
                context.getString(messageId)
            }
            remainingDays < -1 -> {
                val messageId = R.string.flower_frequency_late_days_label
                String.format(context.getString(messageId), remainingDays * -1)
            }
            remainingDays == 1 -> {
                context.getString(R.string.flower_frequency_tomorrow_label)
            }
            else ->
                String.format(context.getString(R.string.flower_frequency_in_days_label), remainingDays)
        })
    }
}