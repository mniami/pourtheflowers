package guideme.bydgoszcz.pl.pourtheflower.views.fragments

import android.graphics.Bitmap

interface TakingPictureThumbnail {
    fun onThumbnail(bitmap: Bitmap)
    fun onPictureCaptured()
}