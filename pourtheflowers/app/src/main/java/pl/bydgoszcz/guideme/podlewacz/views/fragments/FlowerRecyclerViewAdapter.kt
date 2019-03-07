package pl.bydgoszcz.guideme.podlewacz.views.fragments

import android.content.Context
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import pl.bydgoszcz.guideme.podlewacz.R
import pl.bydgoszcz.guideme.podlewacz.features.PouredTheFlower
import pl.bydgoszcz.guideme.podlewacz.views.fragments.providers.RemainingDaysMessageProvider
import pl.bydgoszcz.guideme.podlewacz.views.fragments.providers.ShortDesriptionProvider
import pl.bydgoszcz.guideme.podlewacz.views.model.UiItem
import pl.bydgoszcz.guideme.podlewacz.notifications.updateRemainingTime
import pl.bydgoszcz.guideme.podlewacz.utils.getColorFromResource
import pl.bydgoszcz.guideme.podlewacz.views.fragments.FlowerListFragment.OnListFragmentInteractionListener
import kotlinx.android.synthetic.main.fragment_flower_item.view.*

class FlowerRecyclerViewAdapter(
        var items: List<UiItem>,
        private val mContext: Context,
        private val mListener: OnListFragmentInteractionListener?,
        private val pouredTheFlower: PouredTheFlower)
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

        holder.mNameView.text = item.item.name
        holder.mDescriptionView.text = ShortDesriptionProvider.provide(item.item.description)

        if (showRemainingTime) {
            holder.mFrequencyText.text = RemainingDaysMessageProvider.provide(mContext, item)
            holder.mFrequencyText.visibility = View.VISIBLE
        } else {
            holder.mFrequencyText.visibility = View.GONE
        }

        val color = if (item.remainingTime.toDays() < 0) android.R.color.holo_red_dark else R.color.colorPrimary

        holder.mFrequencyText.setTextColor(mContext.resources.getColorFromResource(color))
        holder.mView.tag = position
        holder.mBtnPouredFlower.tag = position

        loadImage(holder, item, color, position)

        holder.mBtnPouredFlower.visibility = if (isPourButtonVisible(item)) View.VISIBLE else View.GONE
        holder.mView.setOnClickListener(mOnClickListener)
        holder.mBtnPouredFlower.setOnClickListener { view ->
            val itemPosition = view.tag as Int
            val itemClicked = items[itemPosition]
            pouredTheFlower.pour(itemClicked, view) {
                notifyItemChanged(itemPosition)
            }
        }
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
                mContext.resources.getColorFromResource(color),
                borderSize = 4,
                onError = {
                    // noop
                })
    }

    private fun refresh(holder: ViewHolder) {
        val position = holder.mView.tag as Int? ?: return
        val item = items[position]

        item.updateRemainingTime()

        val color = if (item.remainingTime.toDays() < 0) android.R.color.holo_red_dark else R.color.colorPrimary

        holder.mFrequencyText.text = RemainingDaysMessageProvider.provide(mContext, item)
        holder.mFrequencyText.setTextColor(mContext.resources.getColorFromResource(color))
        holder.mBtnPouredFlower.visibility = if (isPourButtonVisible(item)) View.VISIBLE else View.GONE

        loadImage(holder, item, color, position)
    }

    fun stop() {
        stopped = true
    }

    override fun getItemCount(): Int = items.size

    private fun isPourButtonVisible(item: UiItem): Boolean = item.item.notification.enabled && item.remainingTime.toDays() <= 0

    fun resume() {
        stopped = false
    }

    inner class ViewHolder(val mView: View, isStopped: () -> Boolean) : RecyclerView.ViewHolder(mView) {
        val mNameView: TextView = mView.name
        val mDescriptionView: TextView = mView.description
        val mFlowerImageView: ImageView = mView.flowerImage
        val mBtnPouredFlower: ImageButton = mView.btnPouredFlower
        val mFrequencyText: TextView = mView.remainingDaysTextView
        val worker: Worker = Worker.constructAndRun(isStopped, this)

        override fun toString(): String {
            return super.toString() + " '" + mNameView.text + "'"
        }
    }

    class Worker(val isStopped: () -> Boolean, val holder: ViewHolder) {
        private val REFRESH_DELAY = 5000L
        private val handler: Handler = Handler()

        var onTick: (ViewHolder) -> Unit = {}

        fun run() {
            if (isStopped()) {
                return
            }
            handler.postDelayed({
                onTick(holder)
                run()
            }, REFRESH_DELAY)
        }

        companion object {
            fun constructAndRun(isStopped: () -> Boolean, holder: ViewHolder): Worker {
                val worker = Worker(isStopped, holder)
                worker.run()
                return worker
            }
        }
    }
}
