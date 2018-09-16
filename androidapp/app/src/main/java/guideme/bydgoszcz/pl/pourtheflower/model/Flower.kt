package guideme.bydgoszcz.pl.pourtheflower.model

import java.io.Serializable

/**
 * A dummy item representing a piece of content.
 */
data class Flower(val id: String, val content: String, val description: String, val frequency: Int, val imageUrl: String) : Serializable {
    override fun toString(): String = content

}