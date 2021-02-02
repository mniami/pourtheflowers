package pl.bydgoszcz.guideme.podlewacz.views.fragments

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_flower_item.view.*
import pl.bydgoszcz.guideme.podlewacz.R
import pl.bydgoszcz.guideme.podlewacz.notifications.getElapsedTime
import pl.bydgoszcz.guideme.podlewacz.notifications.getRemainingDaysMessage
import pl.bydgoszcz.guideme.podlewacz.notifications.updateRemainingTime
import pl.bydgoszcz.guideme.podlewacz.threads.runInBackground
import pl.bydgoszcz.guideme.podlewacz.threads.runOnUi
import pl.bydgoszcz.guideme.podlewacz.utils.getColorFromResource
import pl.bydgoszcz.guideme.podlewacz.views.fragments.FlowerListFragment.OnListFragmentInteractionListener
import pl.bydgoszcz.guideme.podlewacz.views.model.UiItem

class FlowerRecyclerViewAdapter(
        var items: List<UiItem>,
        private val mContext: Context,
        private val mListener: OnListFragmentInteractionListener?)
    : RecyclerView.Adapter<FlowerRecyclerViewAdapter.ViewHolder>() {
    private val mOnClickListener: View.OnClickListener
    private var stopped = false

    init {
        mOnClickListener = View.OnClickListener { v ->
            val position = v.tag as Int? ?: return@OnClickListener
            val item = items[position]
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

        holder.mNameView.text = Html.fromHtml(item.item.name)

        if (showRemainingTime) {
            holder.mFrequencyText.text = item.item.notification.getRemainingDaysMessage(mContext)
            holder.mFrequencyText.visibility = View.VISIBLE
        } else {
            holder.mFrequencyText.visibility = View.GONE
        }

        val color = if (item.remainingTime.toDays() < 0) android.R.color.holo_red_dark else R.color.colorPrimary

        holder.mFrequencyText.setTextColor(mContext.resources.getColorFromResource(color))
        holder.mView.tag = position

        loadImage(holder, item, color, position)

        holder.mView.setOnClickListener(mOnClickListener)
        holder.worker.onTick = {
            refresh(it)
        }
    }

    data class ImageItemTag(val color: Int, val position: Int)

    private fun loadImage(holder: ViewHolder, item: UiItem, color: Int, position: Int) {
        val imageItemTag = holder.mFlowerImageView.tag as ImageItemTag?
        if (imageItemTag?.color == color && imageItemTag.position == position) {
            return
        }
        if (item.item.imageUrl.isEmpty()) {
            return
        }
        holder.mFlowerImageView.tag = ImageItemTag(color, position)
        ImageLoader.setImageWithCircle(holder.mFlowerImageView,
                item.item.imageUrl,
                onError = {
                    // noop
                })
    }

    private fun refresh(holder: ViewHolder) {
        val position = holder.mView.tag as Int? ?: return
        val item = items[position]
        if (!item.item.notification.enabled) {
            return
        }
        runInBackground {
            item.updateRemainingTime()
            runOnUi {
                val color = if (item.item.notification.getElapsedTime().toDays() < 0) android.R.color.holo_red_dark else R.color.colorPrimary

                holder.mFrequencyText.text = item.item.notification.getRemainingDaysMessage(mContext)
                holder.mFrequencyText.setTextColor(mContext.resources.getColorFromResource(color))

                loadImage(holder, item, color, position)
            }
        }
    }

    fun stop() {
        stopped = true
    }

    override fun getItemCount(): Int = items.size

    fun resume() {
        stopped = false
    }

    inner class ViewHolder(val mView: View, isStopped: () -> Boolean) : RecyclerView.ViewHolder(mView) {
        val mNameView: TextView = mView.name
        val mFlowerImageView: ImageView = mView.flowerImage
        val mFrequencyText: TextView = mView.remainingDaysTextView
        val worker: Worker = Worker.constructAndRun(isStopped, this)

        override fun toString(): String {
            return super.toString() + " '" + mNameView.text + "'"
        }
    }

}
