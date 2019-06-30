package pl.bydgoszcz.guideme.podlewacz.views.fragments.library

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.tag_list_item.view.*
import pl.bydgoszcz.guideme.podlewacz.R
import pl.bydgoszcz.guideme.podlewacz.model.Tag

class TagsAdapter(private val tags: List<Tag>) : RecyclerView.Adapter<TagsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.tag_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tags.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = tags[position]
        holder.tvName.text = item.value
        holder.vImage.setLetter(item.value[0].toString())
    }

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val tvName = mView.tvName
        val vImage = mView.vImage
    }
}