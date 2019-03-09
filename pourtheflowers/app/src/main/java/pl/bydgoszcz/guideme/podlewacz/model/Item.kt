package pl.bydgoszcz.guideme.podlewacz.model

import java.io.Serializable

data class Item(val id: String = "",
                var name: String = "",
                var description: String = "",
                val imageUrl: String = "",
                val tags: List<Tag> = emptyList(),
                val frequency: Int = 0,
                val notification: Notification = Notification()) : Serializable {
    override fun toString(): String = name
}