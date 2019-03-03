package guideme.bydgoszcz.pl.pourtheflower.views.fragments

import android.graphics.Color
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import guideme.bydgoszcz.pl.pourtheflower.utils.CircleTransform
import guideme.bydgoszcz.pl.pourtheflower.utils.afterMeasured
import java.io.File

object ImageLoader {
    fun loadCircle(itemImage: ImageView, flowerUiItem: UiItem, borderColor: Int = Color.WHITE, borderSize: Int = 7) {
        val parentView = itemImage.parent as ViewGroup
        parentView.afterMeasured {
            if (flowerUiItem.item.imageUrl.isEmpty()) {
                return@afterMeasured
            }
            val imageUrl = flowerUiItem.item.imageUrl
            setImage(itemImage, imageUrl, parentView.measuredWidth, parentView.measuredHeight, borderColor, borderSize, onError = {
                // noop
            })
        }
    }

    fun loadSimple(itemImage: ImageView, imageUrl: String) {
        val parentView = itemImage.parent as ViewGroup

        parentView.afterMeasured {
            val width = parentView.width
            val height = parentView.height

            Picasso.get().load(imageUrl)
                    .resize(width, height)
                    .centerCrop()
                    .into(itemImage)
        }
    }

    fun setImage(itemImage: ImageView, imageUrl: String, width: Int, height: Int, borderColor: Int = Color.WHITE, borderSize: Int = 7, onError: () -> Unit) {
        try {
            Picasso.get().load(imageUrl)
                    .error(android.R.drawable.stat_notify_error)
                    .resize(width, height)
                    .centerInside()
                    .transform(CircleTransform(borderColor, borderSize))
                    .into(itemImage)
        } catch (ex: IllegalArgumentException) {
            onError()
        }
    }

    fun setImage(itemImage: ImageView, file: File, width: Int, height: Int, borderColor: Int = Color.WHITE, borderSize: Int = 7, onError: () -> Unit) {
        try {
            Picasso.get().load(file)
                    .error(android.R.drawable.stat_notify_error)
                    .resize(width, height)
                    .centerInside()
                    .transform(CircleTransform(borderColor, borderSize))
                    .into(itemImage)
        } catch (ex: IllegalArgumentException) {
            onError()
        }
    }
}