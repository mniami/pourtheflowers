package guideme.bydgoszcz.pl.pourtheflower.model

import android.content.Context
import android.text.Html
import android.text.Spanned
import guideme.bydgoszcz.pl.pourtheflower.R

object RemainingDaysMessageProvider {
    fun provide(context : Context, item : UiItem, includeAmount : Boolean = true) : Spanned {
        val remainingDays = item.remainingTime.toDays()
        return Html.fromHtml(when {
            remainingDays == 0 -> {
                val messageId = if (includeAmount) R.string.flower_frequency_today_label else R.string.flower_frequency_today_label_without_amount_header
                context.getString(messageId)
            }
            remainingDays == -1 -> {
                val messageId = if (includeAmount) R.string.flower_frequency_late_yesterday_label else R.string.flower_frequency_in_days_label
                context.getString(messageId)
            }
            remainingDays < -1 -> {
                val messageId = if (includeAmount) R.string.flower_frequency_late_days_label else R.string.flower_frequency_late_days_label_without_amount
                String.format(context.getString(messageId), remainingDays * -1)
            }
            else ->
                String.format(context.getString(R.string.flower_frequency_in_days_label), remainingDays)
        })
    }
}