package pl.bydgoszcz.guideme.podlewacz.views.custom

import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.view.View
import pl.bydgoszcz.guideme.podlewacz.R

class LetterView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var textY: Float = 0f
    private var textColor: Int = Color.BLACK
    private var textX: Float = 0f
    private var letter = ""
    private var textHeight = 14f
    private val textPaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = textColor
        if (textHeight == 0f) {
            textHeight = textSize
        } else {
            textSize = textHeight
        }
    }

    private val shadowPaint = Paint(0).apply {
        color = 0x101010
        maskFilter = BlurMaskFilter(8f, BlurMaskFilter.Blur.NORMAL)
    }

    init {
        context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.LetterView,
                0, 0).apply {
            try {
                val l = getText(R.styleable.LetterView_letter)
                if (l != null) {
                    letter = l.toString()
                }
            } finally {
                recycle()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Try for a width based on our minimum
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)

        val textWidth = textPaint.measureText(letter)
        // Whatever the width ends up being, ask for a height that would let the letter
        // get as big as it can

        val minh: Int = MeasureSpec.getSize(w) - textWidth.toInt() + paddingBottom + paddingTop
        val h: Int = resolveSizeAndState(
                MeasureSpec.getSize(w) - textWidth.toInt(),
                heightMeasureSpec,
                0
        )

        textX = w / 2 + textWidth / 2
        textY = h / 2 + textHeight / 2

        setMeasuredDimension(w, h)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.apply {
            // Draw the shadow
            //drawOval(shadowBounds, shadowPaint)

            // Draw the label text
            drawText(letter, textX, textY, textPaint)

            // Draw the pointer
            //drawLine(textX, pointerY, pointerX, pointerY, textPaint)
            //drawCircle(pointerX, pointerY, pointerSize, mTextPaint)
        }
    }

    fun setLetter(letter: String) {
        this.letter = letter
        invalidate()
        requestLayout()
    }
}