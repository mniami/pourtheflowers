package pl.bydgoszcz.guideme.podlewacz.views.fragments

import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.webkit.URLUtil
import android.widget.ImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import pl.bydgoszcz.guideme.podlewacz.R
import pl.bydgoszcz.guideme.podlewacz.utils.CircleTransform
import java.io.File

object ImageLoader {
    private const val TAG = "ImageLoader"

    fun setImage(itemImage: ImageView, imageUrl: String, onError: () -> Unit = {}) {
        val uri = getImageUri(imageUrl)
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

    fun setImageWithCircle(itemImage: ImageView, imageUrl: String, borderColor: Int = Color.WHITE, borderSize: Int = 7, onError: () -> Unit) {
        val uri = getImageUri(imageUrl)
        Picasso.get().load(uri)
                .error(R.drawable.watering_can_grey)
                .transform(CircleTransform(borderColor, borderSize))
                .into(itemImage, object : Callback {
                    override fun onError(e: Exception?) {
                        Log.e(TAG, e?.message)
                        onError()
                    }

                    override fun onSuccess() {
                        // noop
                    }
                })
    }

    private fun getImageUri(imageUrl: String): Uri {
        return if (imageUrl.startsWith("android.resource") || URLUtil.isValidUrl(imageUrl)) {
            Uri.parse(imageUrl)
        } else {
            Uri.fromFile(File(imageUrl))
        }
    }
}