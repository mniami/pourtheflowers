package guideme.bydgoszcz.pl.pourtheflower.views.fragments

import android.graphics.Color
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import guideme.bydgoszcz.pl.pourtheflower.R
import guideme.bydgoszcz.pl.pourtheflower.utils.CircleTransform
import guideme.bydgoszcz.pl.pourtheflower.utils.afterMeasured
import java.io.File

object ImageLoader {
    private fun afterMeasured(view: View, onError: () -> Unit, success: (width: Int, height: Int) -> Unit) {
        val parentView = view.parent as ViewGroup

        parentView.afterMeasured {
            val width = parentView.width
            val height = parentView.height

            try {
                success(width, height)
            } catch (ex: IllegalArgumentException) {
                onError()
            }
        }
    }

    fun setImage(itemImage: ImageView, imageUrl: String, onError: () -> Unit = {}) {
        afterMeasured(itemImage, onError) { _, _ ->
            val uri = if (imageUrl.contains("http:")) Uri.parse(imageUrl) else Uri.fromFile(File(imageUrl))
            Picasso.get().load(uri)
                    .error(R.drawable.watering_can_grey)
                    .into(itemImage, object : Callback {
                        override fun onError(e: Exception?) {
                            onError()
                        }

                        override fun onSuccess() {
                            // noop
                        }
                    })
        }
    }

    fun setImageWithCircle(itemImage: ImageView, imageUrl: String, borderColor: Int = Color.WHITE, borderSize: Int = 7, onError: () -> Unit) {
        afterMeasured(itemImage, onError) { _, _ ->
            Picasso.get().load(imageUrl)
                    .error(R.drawable.watering_can_grey)
                    .transform(CircleTransform(borderColor, borderSize))
                    .into(itemImage, object : Callback {
                        override fun onError(e: Exception?) {
                            onError()
                        }

                        override fun onSuccess() {
                            // noop
                        }
                    })
        }
    }
}