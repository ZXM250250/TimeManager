package com.example.myapplication.util

import android.view.animation.LinearInterpolator

class JellyInterpolator: LinearInterpolator() {

    private var factor = 0f

    fun JellyInterpolator() {
        factor = 0.15f
    }

    override fun getInterpolation(input: Float): Float {
        return (Math.pow(2.0, -10 * input.toDouble())
                * Math.sin((input - factor / 4) * (2 * Math.PI) / factor) + 1).toFloat()
    }

}