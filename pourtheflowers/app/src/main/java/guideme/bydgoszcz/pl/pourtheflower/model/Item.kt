package guideme.bydgoszcz.pl.pourtheflower.model

import java.io.Serializable

data class Item(val id: String, val content: String, val description: String, val imageUrl: String, val tags : List<Tag>) : Serializable {
    override fun toString(): String = content
}

data class UiItem(val item: Item, var isUser: Boolean) : Serializable