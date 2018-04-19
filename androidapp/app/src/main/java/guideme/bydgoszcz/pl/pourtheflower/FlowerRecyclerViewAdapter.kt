package guideme.bydgoszcz.pl.pourtheflower

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


import guideme.bydgoszcz.pl.pourtheflower.FlowerFragment.OnListFragmentInteractionListener
import guideme.bydgoszcz.pl.pourtheflower.dummy.FlowersContent.FlowerItem

import kotlinx.android.synthetic.main.fragment_flower.view.*

/**
 * [RecyclerView.Adapter] that can display a [FlowerItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class FlowerRecyclerViewAdapter(
        private val mValues: List<FlowerItem>,
        private val mListener: OnListFragmentInteractionListener?)
    : RecyclerView.Adapter<FlowerRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as FlowerItem
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_flower, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mNameView.text = item.content
        holder.mDescriptionView.text = item.description
        holder.mFrequencyView.text = item.frequency.toString()

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mNameView: TextView = mView.name
        val mDescriptionView: TextView = mView.description
        val mFrequencyView: TextView = mView.frequency

        override fun toString(): String {
            return super.toString() + " '" + mNameView.text + "'"
        }
    }
}
