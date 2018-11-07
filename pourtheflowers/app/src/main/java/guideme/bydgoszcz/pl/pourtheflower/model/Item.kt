package guideme.bydgoszcz.pl.pourtheflower.model

import java.io.Serializable

data class Item(val id: String,
                var content: String,
                var description: String,
                val imageUrl: String,
                val tags: List<Tag>,
                val notification: Notification) : Serializable {
    override fun toString(): String = content
}

data class UiItem(val item: Item, var isUser: Boolean) : Serializable