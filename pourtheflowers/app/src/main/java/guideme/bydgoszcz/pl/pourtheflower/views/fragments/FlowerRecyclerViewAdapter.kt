package guideme.bydgoszcz.pl.pourtheflower.views.fragments


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import guideme.bydgoszcz.pl.pourtheflower.R
import guideme.bydgoszcz.pl.pourtheflower.model.Item
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import guideme.bydgoszcz.pl.pourtheflower.views.fragments.FlowerListFragment.OnListFragmentInteractionListener
import kotlinx.android.synthetic.main.fragment_flower_item.view.*

/**
 * [RecyclerView.Adapter] that can display a [Item] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class FlowerRecyclerViewAdapter(
        private val mValues: List<UiItem>,
        private val mListener: OnListFragmentInteractionListener?)
    : RecyclerView.Adapter<FlowerRecyclerViewAdapter.ViewHolder>() {
    private val mOnClickListener: View.OnClickListener
    private var filteredList = mValues

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as UiItem
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

        holder.mNameView.text = item.item.name
        holder.mDescriptionView.text = getShorted(item.item.description)
        holder.mView.post {
            if (item.item.imageUrl.isEmpty()) {
                return@post
            }
            Picasso.get()
                    .load(item.item.imageUrl)
                    .resize(holder.mView.measuredWidth, holder.mView.measuredHeight)
                    .centerInside()
                    .into(holder.mFlowerImageView)
        }
        holder.mView.tag = item
        holder.mView.setOnClickListener(mOnClickListener)
    }

    fun filter(text: String) {
        if (text.isBlank()) {
            filteredList = mValues
        } else {
            filteredList = mValues.filter { item ->
                item.item.name.contains(text, true) || item.item.description.contains(text, true)
            }
        }
        notifyDataSetChanged()
    }

    private fun getShorted(description: String): CharSequence? {
        val maxLength = 100
        if (description.length > maxLength){
            val idx = description.indexOfAny(charArrayOf(',','.'), maxLength)
            return if (idx > -1){
                description.indexOfAny(charArrayOf(',','.'))
                description.substring(0, idx) + "."
            } else {
                description.substring(0, maxLength)
            }
        }
        return description
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mNameView: TextView = mView.name
        val mDescriptionView: TextView = mView.description
        val mFlowerImageView: ImageView = mView.flowerImage

        override fun toString(): String {
            return super.toString() + " '" + mNameView.text + "'"
        }
    }
}
