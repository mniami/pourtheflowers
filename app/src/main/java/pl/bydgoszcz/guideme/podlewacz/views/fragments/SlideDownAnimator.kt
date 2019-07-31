package pl.bydgoszcz.guideme.podlewacz.views.fragments

import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

class SlideDownAnimator {
    fun animate(view : View) {
        val valueAnimator = ValueAnimator.ofFloat(1f, 0f, 1f)
        valueAnimator.interpolator = AccelerateDecelerateInterpolator() // increase the speed first and then decrease
        valueAnimator.duration = 700
        valueAnimator.addUpdateListener {
            view.scaleY = it.animatedValue as Float
        }
        valueAnimator.start()
    }
}
