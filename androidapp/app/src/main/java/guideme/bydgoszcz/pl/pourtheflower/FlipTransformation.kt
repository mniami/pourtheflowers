package guideme.bydgoszcz.pl.pourtheflower

import android.graphics.Bitmap
import android.graphics.Matrix
import com.squareup.picasso.Transformation

class FlipTransformation : Transformation {
    override fun key(): String {
        return "flip"
    }

    override fun transform(source: Bitmap?): Bitmap {
        if (source == null) {
            return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        }
        val matrix = Matrix()
        matrix.postScale(-1f, 1f, source.width / 2f, source.height / 2f)

        val newBitmap = Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)

        source.recycle()

        return newBitmap
    }
}