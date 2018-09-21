package guideme.bydgoszcz.pl.pourtheflower.model

import java.io.Serializable

data class Flower(val id: String, val content: String, val description: String, val frequency: Int, val imageUrl: String, var tag: Any = Any()) : Serializable {
    override fun toString(): String = content
}

data class FlowerUiItem(val flower: Flower, var isUser: Boolean) : Serializable