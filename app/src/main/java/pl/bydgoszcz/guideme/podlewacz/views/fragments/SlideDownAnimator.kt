package pl.bydgoszcz.guideme.podlewacz.views.fragments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

class SlideDownAnimator {
    fun animate(view : View) {
        val valueAnimator = ValueAnimator.ofFloat(0f, 500f)
        valueAnimator.interpolator = AccelerateDecelerateInterpolator() // increase the speed first and then decrease
        valueAnimator.duration = 700
        valueAnimator.addUpdateListener {
            view.translationY = it.animatedValue as Float
        }
        valueAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                view.translationY = 0f
            }
        })
        valueAnimator.start()
    }
}
