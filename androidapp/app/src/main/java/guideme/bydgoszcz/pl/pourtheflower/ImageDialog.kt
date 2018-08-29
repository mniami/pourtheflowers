package guideme.bydgoszcz.pl.pourtheflower

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fullscreen_image_dialog.*


class ImageDialog : DialogFragment() {
    val IMAGE_URL = "imageUrl"

    private var scaleFactor = 1.0f
    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var imageUrl: String? = null

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        imageUrl = args?.getString(IMAGE_URL)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fullscreen_image_dialog, container, false)
    }

    override fun onResume() {
        super.onResume()
        val url = imageUrl
        if (url != null) {
            Picasso.get().load(url)
                    .centerInside()
                    .transform(FlipTransformation(url))
                    .into(imageView)
        }
        scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())

        imageView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                scaleGestureDetector?.onTouchEvent(p1)
                return true
            }
        })
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            val detector = scaleGestureDetector
            scaleFactor *= detector.scaleFactor
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 10.0f))
            imageView.scaleX = scaleFactor
            imageView.scaleY = scaleFactor
            return true;
        }
    }
}