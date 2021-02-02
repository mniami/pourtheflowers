package pl.bydgoszcz.guideme.podlewacz.utils

import android.graphics.*
import com.squareup.picasso.Transformation


class CircleTransform(private val borderColor: Int, private val borderSize: Int = 7) : Transformation {
    override fun transform(source: Bitmap): Bitmap {
        val size = Math.min(source.width, source.height)

        val x = (source.width - size) / 2
        val y = (source.height - size) / 2

        val squaredBitmap = Bitmap.createBitmap(source, x, y, size, size)
        if (squaredBitmap != source) {
            source.recycle()
        }

        val bitmap = Bitmap.createBitmap(size, size, source.config)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        val shader = BitmapShader(squaredBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

        paint.shader = shader
        paint.isAntiAlias = true

        val r = size / 15f
        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        borderPaint.color = borderColor

        val border2Paint = Paint(Paint.ANTI_ALIAS_FLAG)
        border2Paint.color = Color.WHITE

        val rect = RectF(0f, 0f, size.toFloat(), size.toFloat())
        canvas.drawRoundRect(rect, r, r, paint)
//        canvas.drawRoundRect(rect, r, r, borderPaint)

        squaredBitmap.recycle()
        return bitmap
    }

    override fun key(): String {
        return "circle"
    }
}
