package com.example.myapplication.adapter


import android.view.View
import androidx.viewpager.widget.ViewPager


class RotateYTransformer: ViewPager.PageTransformer {
    private var mMaxRotate = 35f
    override fun transformPage(view: View, position: Float) {
        view.pivotY = view.height / 2.toFloat()
        if (position < -1) { // [-Infinity,-1)
            // 左边的碎片
            view.rotationY = -1 * mMaxRotate
            view.pivotX = view.width.toFloat()
        } else if (position <= 1) { // [-1,1]
            // 看到的碎片
            view.pivotX = view.width * 0.5f * (1 - position)
            view.rotationY = position * mMaxRotate

        } else { // (1,+Infinity]
            // 右边的碎片
            view.rotationY = 1 * mMaxRotate
            view.pivotX = 0f
        }
    }
}