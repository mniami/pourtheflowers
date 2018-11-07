package guideme.bydgoszcz.pl.pourtheflower.views.fragments

import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import guideme.bydgoszcz.pl.pourtheflower.utils.FlipTransformation
import guideme.bydgoszcz.pl.pourtheflower.utils.afterMeasured
import java.net.URL

class ImageLoader(private val itemImage: ImageView) {
    fun load(flowerUiItem: UiItem) {
        val parentView = itemImage.parent as ViewGroup
        parentView.afterMeasured {
            if (flowerUiItem.item.imageUrl.isEmpty()) {
                return@afterMeasured
            }
            val imageUrl = flowerUiItem.item.imageUrl
//            if (!URLUtil.isFileUrl(imageUrl) && !URLUtil.isNetworkUrl(imageUrl)) {
//                return@afterMeasured
//            }
            val description = String.format("Source: %s", URL(imageUrl).host)

            Picasso.get().load(imageUrl)
                    .resize(parentView.measuredWidth, parentView.measuredHeight)
                    .centerInside()
                    .transform(FlipTransformation(description))
                    .into(itemImage)
        }
    }
}