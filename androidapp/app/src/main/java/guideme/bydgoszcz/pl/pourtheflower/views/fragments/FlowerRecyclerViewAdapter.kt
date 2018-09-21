package guideme.bydgoszcz.pl.pourtheflower.views.fragments


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import guideme.bydgoszcz.pl.pourtheflower.R
import guideme.bydgoszcz.pl.pourtheflower.model.Flower
import guideme.bydgoszcz.pl.pourtheflower.model.FlowerUiItem
import guideme.bydgoszcz.pl.pourtheflower.utils.getColorFromResource
import guideme.bydgoszcz.pl.pourtheflower.views.fragments.FlowerListFragment.OnListFragmentInteractionListener
import kotlinx.android.synthetic.main.fragment_flower_item.view.*

/**
 * [RecyclerView.Adapter] that can display a [Flower] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class FlowerRecyclerViewAdapter(
        private val mValues: List<FlowerUiItem>,
        private val mListener: OnListFragmentInteractionListener?)
    : RecyclerView.Adapter<FlowerRecyclerViewAdapter.ViewHolder>() {
    private val mOnClickListener: View.OnClickListener
    private var filteredList = mValues

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as FlowerUiItem
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_flower_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        val backgroundColorResource = if (item.isUser) R.color.user_item_background else R.color.default_item_background

        holder.itemView.setBackgroundColor(holder.itemView.resources.getColorFromResource(backgroundColorResource))
        holder.mNameView.text = item.flower.content
        holder.mDescriptionView.text = getShorted(item.flower.description)
        holder.mFrequencyView.text = item.flower.frequency.toString()

        holder.mView.post {
            if (item.flower.imageUrl.isEmpty()) {
                return@post
            }
            Picasso.get()
                    .load(item.flower.imageUrl)
                    .resize(holder.mView.measuredWidth, holder.mView.measuredHeight)
                    .centerInside()
                    .into(holder.mFlowerImageView)
        }
        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    fun filter(text: String) {
        if (text.isBlank()) {
            filteredList = mValues
        } else {
            filteredList = mValues.filter { item ->
                item.flower.content.contains(text, true) || item.flower.description.contains(text, true)
            }
        }
        notifyDataSetChanged()
    }

    private fun getShorted(description: String): CharSequence? {
        val maxLength = 100
        if (description.length > maxLength){
            val idx = description.indexOfAny(charArrayOf(',','.'), maxLength)
            if (idx > -1){
                description.indexOfAny(charArrayOf(',','.'))
                return description.substring(0, idx) + "."
            }
            else {
                return description.substring(0, maxLength)
            }
        }
        return description
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mNameView: TextView = mView.name
        val mDescriptionView: TextView = mView.description
        val mFrequencyView: TextView = mView.frequency
        val mFlowerImageView: ImageView = mView.flowerImage

        override fun toString(): String {
            return super.toString() + " '" + mNameView.text + "'"
        }
    }
}
