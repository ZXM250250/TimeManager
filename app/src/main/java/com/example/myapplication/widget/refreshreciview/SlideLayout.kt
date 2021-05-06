package com.example.myapplication.widget.refreshreciview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.FrameLayout
import android.widget.Scroller


class SlideLayout@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): FrameLayout(context, attrs, defStyleAttr) {


    private val contentView by lazy {
        getChildAt(0)
    }
    private val menuView by lazy {
        getChildAt(1)
    }

    /**
     * 滚动者
     */
    private val scroller by lazy {
        Scroller(context)
    }

    /**
     * Content的宽
     */
    private val contentWidth by lazy {
        contentView.getMeasuredWidth()
    }
    private val menuWidth by lazy {
        menuView.getMeasuredWidth()
    }
    private val viewHeight //他们的高都是相同的
           by lazy {
               getMeasuredHeight()
           }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        contentWidth
        menuWidth
        viewHeight
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        menuView.layout(contentWidth, 0, contentWidth + menuWidth, viewHeight)
    }

    private var startX = 0f
    private var startY = 0f
    private var downX //只赋值一次
            = 0f
    private var  downY = 0f
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                onStateChangeListenter!!.onDown(this)
                run {
                    startX = event!!.x
                    downX = startX
                }
                run {
                    startY = event!!.y
                    downY = startY
                }
                Log.e("策划", "SlideLayout-onTouchEvent-ACTION_DOWN")
            }
            MotionEvent.ACTION_MOVE -> {
                Log.e("策划", "SlideLayout-onTouchEvent-ACTION_MOVE")
                //2.记录结束值
                val endX = event!!.x
                val endY = event!!.y
                //3.计算偏移量
                val distanceX = endX - startX
                var toScrollX = (scrollX - distanceX).toInt()
                if (toScrollX < 0) {
                    toScrollX = 0
                } else if (toScrollX > menuWidth) {
                    toScrollX = menuWidth
                }
                scrollTo(toScrollX, scrollY)
                startX = event!!.x
                startY = event!!.y
                //在X轴和Y轴滑动的距离
                val DX = Math.abs(endX - downX)
                val DY = Math.abs(endY - downY)
                if (DX > DY && DX > 8) {
                    //水平方向滑动
                    //响应侧滑
                    //反拦截-事件给SlideLayout
                   parent.requestDisallowInterceptTouchEvent(true)
                }
            }
            MotionEvent.ACTION_UP -> {
                Log.e("策划", "SlideLayout-onTouchEvent-ACTION_UP")
                val totalScrollX = scrollX //偏移量
                if (Math.abs(totalScrollX)<3){
                    performClick()
                }
                if (totalScrollX < menuWidth / 2) {
                    //关闭Menu
                   closeMenu()
                } else {
                    //打开Menu
                  openMenu()
                }
            }
        }

        return true
    }




    /**
     * 打开menu
     */
    fun openMenu() {
        //--->menuWidth
        val distanceX = menuWidth - scrollX
        scroller.startScroll(scrollX, scrollY, distanceX, scrollY)
        invalidate() //强制刷新
        if (onStateChangeListenter != null) {
            onStateChangeListenter!!.onOpen(this)
        }
    }
    override fun computeScroll() {
        super.computeScroll()
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.currX, scroller.currY)
            invalidate()
        }
    }
    /**
     * 关闭menu
     */
    fun closeMenu() {
        //--->0 目标值
        val distanceX = 0 - scrollX
        scroller.startScroll(scrollX, scrollY, distanceX, scrollY)
        invalidate() //强制刷新
        if (onStateChangeListenter != null) {
            onStateChangeListenter!!.onClose(this)
        }
    }

    /**
     * 监听SlideLayout状态的改变
     */
    interface OnStateChangeListenter {
        fun onClose(layout: SlideLayout?)
        fun onDown(layout: SlideLayout?)
        fun onOpen(layout: SlideLayout?)
    }

    private var onStateChangeListenter: OnStateChangeListenter? = null

    /**
     * 设置SlideLayout状态的监听
     * @param onStateChangeListenter
     */
    fun setOnStateChangeListenter(onStateChangeListenter: OnStateChangeListenter?) {
        this.onStateChangeListenter = onStateChangeListenter
    }
}