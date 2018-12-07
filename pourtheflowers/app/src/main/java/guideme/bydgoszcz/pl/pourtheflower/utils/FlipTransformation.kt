package guideme.bydgoszcz.pl.pourtheflower.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import com.squareup.picasso.Transformation

class FlipTransformation(private val textToDraw: String) : Transformation {
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

        //val canvas = Canvas(newBitmap)
        //val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        //paint.style = Paint.Style.FILL
        //paint.textSize = 30f

        //canvas.drawText(textToDraw, 10f, source.height.toFloat() - 2f, paint)

        source.recycle()

        return newBitmap
    }
}