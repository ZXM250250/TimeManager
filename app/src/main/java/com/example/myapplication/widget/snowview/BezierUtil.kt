package com.example.myapplication

import android.graphics.Point

object BezierUtil {
    fun calculatePoint(
        t: Float,
        p0: Point,
        p1: Point,
        p2: Point
    ): Point? {
        val point = Point()
        val temp = 1 - t
        /**
         * 二阶贝塞尔曲线的公式  算出曲线上 点坐标的位置
         */
        point.x = (temp * temp * p0.x + 2 * t * temp * p1.x + t * t * p2.x).toInt()
        point.y = (temp * temp * p0.y + 2 * t * temp * p1.y + t * t * p2.y).toInt()
        return point
    }
}