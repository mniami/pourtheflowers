package guideme.bydgoszcz.pl.pourtheflower.views.fragments


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.PorterDuff
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import guideme.bydgoszcz.pl.pourtheflower.R
import guideme.bydgoszcz.pl.pourtheflower.features.PouredTheFlower
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import guideme.bydgoszcz.pl.pourtheflower.notifications.getBackgroundColor
import guideme.bydgoszcz.pl.pourtheflower.notifications.getPassedTime
import guideme.bydgoszcz.pl.pourtheflower.notifications.getRemainingTime
import guideme.bydgoszcz.pl.pourtheflower.notifications.updateRemainingTime
import guideme.bydgoszcz.pl.pourtheflower.utils.NotificationTime
import guideme.bydgoszcz.pl.pourtheflower.utils.SystemTime
import guideme.bydgoszcz.pl.pourtheflower.utils.getColorFromResource
import guideme.bydgoszcz.pl.pourtheflower.views.fragments.FlowerListFragment.OnListFragmentInteractionListener
import kotlinx.android.synthetic.main.fragment_flower_item.view.*

class FlowerRecyclerViewAdapter(
        var items: List<UiItem>,
        private val mContext: Context,
        private val mListener: OnListFragmentInteractionListener?,
        private val pouredTheFlower: PouredTheFlower)
    : RecyclerView.Adapter<FlowerRecyclerViewAdapter.ViewHolder>() {
    private val mOnClickListener: View.OnClickListener
    private var filteredList = items
    private val handler = Handler()
    private var stopped = false
    private val REFRESH_DELAY = 200L

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as UiItem
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_flower_item, parent, false)
        return ViewHolder(view) { stopped }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val showRemainingTime = item.item.notification.enabled

        item.updateRemainingTime()

        holder.mNameView.text = item.item.name
        holder.mDescriptionView.text = getShorted(item.item.description)

        if (showRemainingTime) {
            holder.mFrequencyText.text = getFrequencyText(item)
            holder.mFrequencyText.visibility = View.VISIBLE
        }
        else {
            holder.mFrequencyText.visibility = View.GONE
        }

        if (item.item.notification.enabled) {
            refreshProgressBar(item, holder)
        } else {
            holder.mFrequencyProgressBar.visibility = View.GONE
        }
        holder.mView.post {
            if (item.item.imageUrl.isEmpty()) {
                return@post
            }
            ImageLoader(holder.mFlowerImageView).setImage(item.item.imageUrl,
                    holder.mView.measuredWidth,
                    holder.mView.measuredHeight,
                    mContext.resources.getColorFromResource(R.color.colorPrimaryDark),
                    borderSize = 4)
        }
        holder.mView.tag = item
        holder.mBtnPouredFlower.tag = item

        holder.mBtnPouredFlower.visibility = if (isPourButtonVisible(item)) View.VISIBLE else View.GONE
        holder.mFrequencyProgressBar.visibility = if (showRemainingTime) View.VISIBLE else View.GONE
        holder.mView.setOnClickListener(mOnClickListener)
    }

    private fun getFrequencyText(item: UiItem): Spanned {
        val remainingDays = item.remainingTime.toDays()
        return if (remainingDays == 0) {
            Html.fromHtml(mContext.getString(R.string.flower_frequency_today_label))
        }
        else {
            Html.fromHtml(String.format(mContext.getString(R.string.flower_frequency_in_days_label), remainingDays))
        }
    }

    private fun refreshProgressBar(item: UiItem, holder: ViewHolder) {
        item.updateRemainingTime()

        val passedTime = item.getPassedTime()
        val maxFrequencyTime = (item.item.notification.repeatInTime - NotificationTime.fromDays(1)).seconds

        holder.mBtnPouredFlower.visibility = if (isPourButtonVisible(item)) View.VISIBLE else View.GONE
        holder.mFrequencyProgressBar.progress = passedTime.seconds
        holder.mFrequencyProgressBar.progressDrawable.setColorFilter(item.item.notification.getBackgroundColor(mContext, item.remainingTime.toDays()),
                PorterDuff.Mode.SRC_IN)
        holder.mFrequencyProgressBar.max = maxFrequencyTime
        holder.mBtnPouredFlower.setOnClickListener { view ->
            val item = view.tag as UiItem
            pouredTheFlower.pour(item) {
                val remainingDays = item.item.notification.getRemainingTime(SystemTime.current()).toDays()
                val message = String.format(mContext.getString(R.string.flower_poured), remainingDays)
                val valueAnimator = ValueAnimator.ofFloat(0f, 500f)

                valueAnimator.interpolator = AccelerateDecelerateInterpolator() // increase the speed first and then decrease
                valueAnimator.duration = 700
                valueAnimator.addUpdateListener {
                    view.translationY = it.animatedValue as Float
                }
                valueAnimator.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        view.translationY = 0f
                    }
                })
                valueAnimator.start()
                Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                        .show()
            }
        }

        if (!holder.isStopped()) {
            handler.postDelayed({ refreshProgressBar(item, holder) }, REFRESH_DELAY)
        }
    }

    private fun isPourButtonVisible(item: UiItem): Boolean = item.item.notification.enabled && item.remainingTime.toDays() == 0

    fun stop() {
        stopped = true
    }

    fun filter(text: String) {
        if (text.isBlank()) {
            filteredList = items
        } else {
            filteredList = items.filter { item ->
                item.item.name.contains(text, true) || item.item.description.contains(text, true)
            }
        }
        notifyDataSetChanged()
    }

    private fun getShorted(description: String): CharSequence? {
        val maxLength = 100
        if (description.length > maxLength) {
            val idx = description.indexOfAny(charArrayOf(',', '.'), maxLength)
            return if (idx > -1) {
                description.indexOfAny(charArrayOf(',', '.'))
                description.substring(0, idx) + "."
            } else {
                description.substring(0, maxLength)
            }
        }
        return description
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(val mView: View, val isStopped: () -> Boolean) : RecyclerView.ViewHolder(mView) {
        val mNameView: TextView = mView.name
        val mDescriptionView: TextView = mView.description
        val mFlowerImageView: ImageView = mView.flowerImage
        val mFrequencyProgressBar: ProgressBar = mView.frequencyProgressBar
        val mBtnPouredFlower: ImageButton = mView.btnPouredFlower
        val mFrequencyText: TextView = mView.frequencyText

        override fun toString(): String {
            return super.toString() + " '" + mNameView.text + "'"
        }
    }
}
