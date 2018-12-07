package guideme.bydgoszcz.pl.pourtheflower.loaders

import guideme.bydgoszcz.pl.pourtheflower.model.Item
import guideme.bydgoszcz.pl.pourtheflower.model.ItemsRepository
import guideme.bydgoszcz.pl.pourtheflower.model.Notification
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import javax.inject.Inject

class ImageLoader @Inject constructor(private val repo: ItemsRepository) {
    //private val loadListEndpoint = "https://picsum.photos/list"
    //private val downloadEndpoint = "https://unsplash.com/photos/{key}/download?force=true"
    //private var cache : Array<PicsumPhotoImage> = emptyArray()

    fun load() {
//        if (cache.isEmpty()) {
//            cache = URL(loadListEndpoint).readJson()
//        }

        repo.user.items.addAll((1..100).map {
            val imageUrl = "https://picsum.photos/200/300?image=$it"
            UiItem(item = Item(id = "$it", name = "Image $it", description = "Description $it", imageUrl = imageUrl, tags = emptyList(),
                    notification = Notification(false, 0, 0),
                    frequency = 0), isUser = true)
        }.take(10))
    }
}

data class PicsumPhotoImage(val format: String,
                            val width: String,
                            val filename: String,
                            val id: String,
                            val author: String,
                            val author_url: String,
                            val post_url: String)

