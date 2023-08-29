package pl.bydgoszcz.guideme.podlewacz.views.fragments.library

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.bydgoszcz.guideme.podlewacz.R
import pl.bydgoszcz.guideme.podlewacz.databinding.TagListItemBinding
import pl.bydgoszcz.guideme.podlewacz.model.Tag

class TagsAdapter(private val tags: List<Tag>) : RecyclerView.Adapter<TagsAdapter.ViewHolder>() {
    private var _binding: TagListItemBinding? = null
    private val binding get() = _binding!!
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        _binding = TagListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return tags.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = tags[position]
        binding.tvName.text = item.value
        holder.vImage.setLetter(item.value[0].toString())
    }

    inner class ViewHolder(binding: TagListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvName = binding.tvName
        val vImage = binding.vImage
    }
}
