package com.example.myapplication

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import java.util.*

class Snow {
    /**
     * 自变量t在 零到一之间的取值  根据雪花运动进度的情况 为贝塞尔中间左边的计算给出自变量
     */
    var x = 0
        get() {
            /**
             * 自变量t在 零到一之间的取值  根据雪花运动进度的情况 为贝塞尔中间左边的计算给出自变量
             */
            val t = y * 1.0f / (endPoint!!.y - startPoint!!.y)
            return BezierUtil.calculatePoint(t, startPoint!!, middlePoint!!, endPoint!!)!!.x
        }
    var y = 0
    var color = 0
    var radius = 0
    var rotation = 0
    var speed = 0

    /**
     * 父布局的宽度
     */
    var parentWidth = 0
    var parentHeight = 0

    /**
     * 给雪花飘落找到三个 贝塞尔的控制点   为它绘制曲线下落
     */
    var startPoint: Point? = null
    var middlePoint: Point? = null
    var endPoint: Point? = null

    var strokeWidth = 0

    /**
     * 逻辑函数
     */
    fun step() {
        y += speed
        /**
         * 如果雪花划出屏幕外边  那就回到上边重新下降
         */
        if (y > parentHeight) {
            y = -50
        }
        /**
         * 每次绘制  移动一个角度  结果是  雪花的旋转特效
         *
         */
        rotation = rotation + 1
    }

    fun onDraw(canvas: Canvas, paint: Paint) {
        canvas.save()
        paint.color = color
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = strokeWidth.toFloat()
        //将画布旋转    相当于旋转了 雪花本身   并且更加的 节省资源
        canvas.rotate(rotation.toFloat(), x.toFloat(), y.toFloat()) //
        /**
         * 绘制一瓣雪花的过程   再重复绘制 过程六次就可以得到完整的雪花
         * 画布的开始和结束   都要将画布保存和取出  重新绘制
         *
         * 可以保证每一片 雪花 绘制时 所拿到的画布都是  白纸一样的
         */
        for (i in 0..5) {
            val lineStartX = x
            val lineEndX = x + radius
            canvas.drawLine(
                lineStartX.toFloat(),
                y.toFloat(),
                lineEndX.toFloat(),
                y.toFloat(),
                paint
            )

            //雪花瓣的上片叶的绘制
            val line1StartX = (x + radius * 0.6).toInt()
            val line1StartY = y
            val degree60 = Math.toRadians(60.0)
            val line1EndX = (line1StartX + radius * 0.4 * Math.cos(degree60)).toInt()
            val line1EndY = (line1StartY - radius * 0.4 * Math.sin(degree60)).toInt()
            canvas.drawLine(
                line1StartX.toFloat(),
                line1StartY.toFloat(),
                line1EndX.toFloat(),
                line1EndY.toFloat(),
                paint
            )

            //雪花瓣的下片叶的绘制
            val line2StartX = (x + radius * 0.6).toInt()
            val line2StartY = y
            val line2EndX = (line1StartX + radius * 0.4 * Math.cos(degree60)).toInt()
            val line2EndY = (line1StartY + radius * 0.4 * Math.sin(degree60)).toInt()
            canvas.drawLine(
                line2StartX.toFloat(),
                line2StartY.toFloat(),
                line2EndX.toFloat(),
                line2EndY.toFloat(),
                paint
            )
            //也是利用画布的选装绘制六瓣
            canvas.rotate(60f, x.toFloat(), y.toFloat())
        }
        canvas.restore()
    }

    companion object {
        fun generateSnow(width: Int, height: Int): Snow {
            val snow = Snow()
            snow.parentWidth = width
            snow.parentHeight = height
            val random = Random()

            /**
             * 雪花最起始被绘制的坐标
             */
            val x = random.nextInt(width)
            val y = -random.nextInt(height)
            val r = random.nextInt(255)
            val g = random.nextInt(255)
            val b = random.nextInt(255)
            val color = Color.argb(255, r, g, b)

            /**
             * 雪花的大小
             */
            val radius = 10 + random.nextInt(10)

            /**
             * 雪花的旋转的角度
             */
            val rotation = random.nextInt(60)

            /**
             * 雪花的飘动速度
             */
            val speed = ((2 + Math.abs(radius - 20)) * 0.5).toInt()

            /**
             * 绘制雪花的时候的   线条的粗细
             */
            val strokeWidth = (radius * 0.2).toInt()

            /**
             * 雪花运动过程中的三个点    三个点的位置各不相同   也就是为了给贝塞尔曲线的绘制  提供 起始点  控制点  终止点
             */
            val start =
                Point(random.nextInt(width), -random.nextInt(height))
            val middle =
                Point(random.nextInt(width), random.nextInt(height))
            val end =
                Point(random.nextInt(width), height + random.nextInt(height))
            snow.x = x
            snow.y = y
            snow.color = color
            snow.radius = radius
            snow.rotation = rotation
            snow.speed = speed
            snow.strokeWidth = strokeWidth
            snow.startPoint = start
            snow.middlePoint = middle
            snow.endPoint = end
            return snow
        }
    }
}