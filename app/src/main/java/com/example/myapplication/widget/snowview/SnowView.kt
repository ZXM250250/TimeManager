package com.example.myapplication

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Handler
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.util.*

class SnowView@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): View(context, attrs, defStyleAttr) {


    private var paint: Paint? = null
    private var snows: MutableList<Snow>? = null
    private val snowNum = 100





   init {
       paint = Paint(Paint.ANTI_ALIAS_FLAG)
       snows = ArrayList()
   }






    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        snows!!.clear()
        for (i in 0 until snowNum) {
            snows!!.add(Snow.generateSnow(w, h))
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        for (i in 0 until snowNum) {
            snows!![i].onDraw(canvas!!, paint!!)
            snows!![i].step()
        }
        Handler().postDelayed({ invalidate() }, 10)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }

}