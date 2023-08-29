package pl.bydgoszcz.guideme.podlewacz.views.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import pl.bydgoszcz.guideme.podlewacz.databinding.FullscreenImageDialogBinding
import pl.bydgoszcz.guideme.podlewacz.views.fragments.ImageLoader

class ImageDialog : DialogFragment() {
    companion object {
        const val IMAGE_URL = "imageUrl"
    }

    private var scaleFactor = 1.0f
    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var imageUrl: String? = null
    private var _binding: FullscreenImageDialogBinding? = null
    private val binding get() = _binding!!

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        imageUrl = args?.getString(IMAGE_URL)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FullscreenImageDialogBinding.inflate(inflater, container, false);
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        imageUrl?.let {
            ImageLoader.setImageWithCircle(binding.vImage, it)
        }
        binding.closeButton.setOnClickListener {
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()

        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT

        dialog?.window?.setLayout(width, height)
    }

    override fun onResume() {
        super.onResume()

        scaleGestureDetector = ScaleGestureDetector(binding.root.context, ScaleListener())

        binding.vImage.setOnTouchListener { _, motionView ->
            scaleGestureDetector?.onTouchEvent(motionView)
            true
        }
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            val detector = scaleGestureDetector
            scaleFactor *= detector.scaleFactor
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 10.0f))
            binding.vImage.scaleX = scaleFactor
            binding.vImage.scaleY = scaleFactor
            return true
        }
    }
}
