package guideme.bydgoszcz.pl.pourtheflower.views.fragments


import android.content.Context
import android.graphics.PorterDuff
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import guideme.bydgoszcz.pl.pourtheflower.R
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import guideme.bydgoszcz.pl.pourtheflower.notifications.getBackgroundColor
import guideme.bydgoszcz.pl.pourtheflower.notifications.getRemainingTime
import guideme.bydgoszcz.pl.pourtheflower.utils.SystemTime
import guideme.bydgoszcz.pl.pourtheflower.utils.getColorFromResource
import guideme.bydgoszcz.pl.pourtheflower.views.fragments.FlowerListFragment.OnListFragmentInteractionListener
import kotlinx.android.synthetic.main.fragment_flower_item.view.*

class FlowerRecyclerViewAdapter(
        var items: List<UiItem>,
        private val mContext: Context,
        private val mListener: OnListFragmentInteractionListener?)
    : RecyclerView.Adapter<FlowerRecyclerViewAdapter.ViewHolder>() {
    private val mOnClickListener: View.OnClickListener
    private var filteredList = items
    private val handler = Handler()
    private var stopped = false

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

        holder.mNameView.text = item.item.name
        holder.mDescriptionView.text = getShorted(item.item.description)

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
        holder.mView.setOnClickListener(mOnClickListener)
    }

    private fun refreshProgressBar(item: UiItem, holder: ViewHolder) {
        item.remainingTime = item.item.notification.getRemainingTime(SystemTime())

        holder.mFrequencyProgressBar.visibility = View.VISIBLE
        holder.mFrequencyProgressBar.progress = (item.item.notification.repeatInTime - item.remainingTime).value
        holder.mFrequencyProgressBar.progressDrawable.setColorFilter(item.item.notification.getBackgroundColor(mContext, item.remainingTime.toDays()),
                PorterDuff.Mode.SRC_IN)
        holder.mFrequencyProgressBar.max = item.item.notification.repeatInTime.value

        if (!holder.isStopped()) {
            handler.postDelayed({ refreshProgressBar(item, holder) }, 2000)
        }
    }

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

        override fun toString(): String {
            return super.toString() + " '" + mNameView.text + "'"
        }
    }
}
