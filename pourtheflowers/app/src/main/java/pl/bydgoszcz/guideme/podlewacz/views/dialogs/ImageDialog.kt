package pl.bydgoszcz.guideme.podlewacz.views.dialogs

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import pl.bydgoszcz.guideme.podlewacz.R
import pl.bydgoszcz.guideme.podlewacz.views.fragments.ImageLoader
import kotlinx.android.synthetic.main.fullscreen_image_dialog.*

class ImageDialog : DialogFragment() {
    companion object {
        const val IMAGE_URL = "imageUrl"
    }

    private var scaleFactor = 1.0f
    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var imageUrl: String? = null

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        imageUrl = args?.getString(IMAGE_URL)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fullscreen_image_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageUrl?.let {
            ImageLoader.setImage(imageView, it)
        }
        closeButton.setOnClickListener {
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()

        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT

        dialog.window?.setLayout(width, height)
    }

    override fun onResume() {
        super.onResume()

        scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())

        imageView.setOnTouchListener { _, motionView ->
            scaleGestureDetector?.onTouchEvent(motionView)
            true
        }
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            val detector = scaleGestureDetector
            scaleFactor *= detector.scaleFactor
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 10.0f))
            imageView.scaleX = scaleFactor
            imageView.scaleY = scaleFactor
            return true
        }
    }
}