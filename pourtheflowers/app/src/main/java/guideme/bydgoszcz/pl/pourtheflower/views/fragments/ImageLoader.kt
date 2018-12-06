package guideme.bydgoszcz.pl.pourtheflower.views.fragments

import android.graphics.Bitmap
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import guideme.bydgoszcz.pl.pourtheflower.utils.CircleTransform
import guideme.bydgoszcz.pl.pourtheflower.utils.FlipTransformation
import guideme.bydgoszcz.pl.pourtheflower.utils.afterMeasured
import java.io.File
import java.net.URL

class ImageLoader(private val itemImage: ImageView) {
    fun load(flowerUiItem: UiItem) {
        val parentView = itemImage.parent as ViewGroup
        parentView.afterMeasured {
            if (flowerUiItem.item.imageUrl.isEmpty()) {
                return@afterMeasured
            }
            val imageUrl = flowerUiItem.item.imageUrl
            setImage(imageUrl,parentView.measuredWidth, parentView.measuredHeight)
        }
    }
    fun setImage(imageUrl : String, width : Int, height : Int) {
        Picasso.get().load(imageUrl)
                .resize(width, height)
                .centerInside()
                .transform(CircleTransform())
                .into(itemImage)
    }
    fun setImage(file : File, width : Int, height : Int) {
        Picasso.get().load(file)
                .resize(width, height)
                .centerInside()
                .transform(CircleTransform())
                .into(itemImage)
    }
}