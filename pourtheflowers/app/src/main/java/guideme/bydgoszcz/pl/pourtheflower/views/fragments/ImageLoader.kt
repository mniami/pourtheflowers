package guideme.bydgoszcz.pl.pourtheflower.views.fragments

import android.graphics.Color
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import guideme.bydgoszcz.pl.pourtheflower.utils.CircleTransform
import guideme.bydgoszcz.pl.pourtheflower.utils.afterMeasured
import java.io.File

class ImageLoader(private val itemImage: ImageView) {
    fun loadCircle(flowerUiItem: UiItem, borderColor: Int = Color.WHITE, borderSize : Int = 7) {
        val parentView = itemImage.parent as ViewGroup
        parentView.afterMeasured {
            if (flowerUiItem.item.imageUrl.isEmpty()) {
                return@afterMeasured
            }
            val imageUrl = flowerUiItem.item.imageUrl
            setImage(imageUrl, parentView.measuredWidth, parentView.measuredHeight, borderColor, borderSize)
        }
    }

    fun loadSimple(uiItem: UiItem){
        val parentView = itemImage.parent as ViewGroup
        val file = uiItem.item.imageUrl

        parentView.afterMeasured {
            val width = parentView.width
            val height = parentView.height

            Picasso.get().load(file)
                    .resize(width, height)
                    .centerCrop()
                    .into(itemImage)
        }
    }

    fun setImage(imageUrl: String, width: Int, height: Int, borderColor: Int = Color.WHITE, borderSize : Int = 7) {
        Picasso.get().load(imageUrl)
                .resize(width, height)
                .centerInside()
                .transform(CircleTransform(borderColor, borderSize))
                .into(itemImage)
    }

    fun setImage(file: File, width: Int, height: Int, borderColor: Int = Color.WHITE, borderSize : Int = 7) {
        Picasso.get().load(file)
                .resize(width, height)
                .centerInside()
                .transform(CircleTransform(borderColor, borderSize))
                .into(itemImage)
    }
}