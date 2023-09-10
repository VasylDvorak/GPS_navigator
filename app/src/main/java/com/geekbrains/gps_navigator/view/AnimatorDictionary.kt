package com.geekbrains.gps_navigator.view

import android.animation.Animator
import android.animation.AnimatorInflater
import android.content.Context
import androidx.fragment.app.FragmentTransaction
import com.geekbrains.gps_navigator.R

class AnimatorDictionary(private val context: Context) {

    fun setAnimator(transit: Int, enter: Boolean): Animator? {
        return when (transit) {
            FragmentTransaction.TRANSIT_FRAGMENT_FADE -> if (enter) {
                AnimatorInflater.loadAnimator(context, R.animator.slide_in)
            } else {
                AnimatorInflater.loadAnimator(context, R.animator.slide_out)
            }

            FragmentTransaction.TRANSIT_FRAGMENT_CLOSE -> if (enter) {
                AnimatorInflater.loadAnimator(context, R.animator.slide_in)
            } else {
                AnimatorInflater.loadAnimator(context, R.animator.slide_out)
            }

            FragmentTransaction.TRANSIT_FRAGMENT_OPEN -> if (enter) {
                AnimatorInflater.loadAnimator(context, R.animator.slide_in)
            } else {
                AnimatorInflater.loadAnimator(context, R.animator.slide_out)
            }

            else -> if (enter) {
                AnimatorInflater.loadAnimator(context, R.animator.slide_in)
            } else {
                AnimatorInflater.loadAnimator(context, R.animator.slide_out)
            }
        }
    }
}