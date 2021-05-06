package com.wlj.aiqiyitabviewtest.aiqiyi


import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.animation.BounceInterpolator
import androidx.annotation.RequiresApi
import com.example.myapplication.R



class TabView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {
    private val padding: Float
    private val paint: Paint
    private val titles = arrayOf("任务", "日历", "成就")
    private val iconRes = intArrayOf(
        R.mipmap.task,
        R.mipmap.calendar,
        R.mipmap.vitory
    )


    private val iconBitmaps: Array<Bitmap?>
    private val iconHeight: Int
    private val fontMetrics =
        Paint.FontMetrics()
    private var selectIndex = 0
    private var valueAnimator: ValueAnimator? = null
    private var animationValue = 2.0f
    private val circlePath = Path()
    private val bgPath = Path()
    private fun sp2px(sp: Int): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sp.toFloat(),
            resources.displayMetrics
        )
    }

    private fun dp2px(dp: Int): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resources.displayMetrics
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val maxHeight =
            (padding * 5 + iconHeight + fontMetrics.descent - fontMetrics.ascent).toInt()
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), maxHeight)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val unitWidth = width * 1.0f / iconRes.size
        val circleMaxRadius = iconHeight / 2 + padding


        val bgLeft = 0f
        val bgTop = padding * 2
        val bgRight = width.toFloat()
        val bgBottom = height.toFloat()
        paint.color = Color.BLACK
        if (animationValue < 1.0f) {
            canvas.drawRect(bgLeft, bgTop, bgRight, bgBottom, paint)
        } else {
            val animationValue2 = animationValue - 1.0f
            val deltY = (1.0f - animationValue2) * padding
            val selectTabCenterX = unitWidth * selectIndex + unitWidth / 2
            val maxMaxCircleRadius = circleMaxRadius + padding
            val bgLeftPaddingPointX = selectTabCenterX - maxMaxCircleRadius - padding
            val bgLeftPointX = selectTabCenterX - maxMaxCircleRadius
            val bgRightPaddingPointX = selectTabCenterX + maxMaxCircleRadius + padding
            val bgRightPointX = selectTabCenterX + maxMaxCircleRadius
            val bgCenterLeft =
                selectTabCenterX - maxMaxCircleRadius * C
            val bgCenterRight =
                selectTabCenterX + maxMaxCircleRadius * C
            /**
             * 底部的黑色区域绘制
             * 并且和动画关联进行绘制
             */
            bgPath.reset()
            bgPath.moveTo(bgLeft, bgTop)
            bgPath.lineTo(bgLeftPaddingPointX, bgTop)
            bgPath.cubicTo(
                bgLeftPointX, bgTop,
                bgCenterLeft, deltY,
                selectTabCenterX, deltY
            )
            bgPath.cubicTo(
                bgCenterRight, deltY,
                bgRightPointX, bgTop,
                bgRightPaddingPointX, bgTop
            )
            bgPath.lineTo(bgRight, bgTop)
            bgPath.lineTo(bgRight, bgBottom)
            bgPath.lineTo(bgLeft, bgBottom)
            bgPath.close()
            canvas.drawPath(bgPath, paint)
        }
        for (i in iconRes.indices) {
            val tabCenterX = unitWidth * i + unitWidth / 2
            val iconTop = padding * 3
            val iconLeft = tabCenterX - iconHeight / 2
            val iconCenterY = iconTop + iconHeight / 2
            val tabTxtBaseLine = height - padding - fontMetrics.descent
            if (selectIndex == i) {

                paint.color = Color.RED
                if (animationValue < 1.0f) {
                    //目前动画第一阶段c
                    val curRadius = animationValue * circleMaxRadius
                    canvas.drawCircle(tabCenterX, iconCenterY, curRadius, paint)
                    canvas.drawBitmap(iconBitmaps[i]!!, iconLeft, iconTop, null)
                } else {

                    val animationValue2 = animationValue - 1.0f
                    val iconCenterY2 = iconCenterY - animationValue2 * padding
                    val iconTop2 = iconTop - animationValue2 * padding
                    var deltaR: Float
                    deltaR = if (animationValue2 < 0.5) {
                        padding * animationValue2
                    } else {
                        (0.5f - (animationValue2 - 0.5f)) * padding
                    }
                    circlePath.reset()
                    circlePath.moveTo(tabCenterX - circleMaxRadius, iconCenterY2)
                    circlePath.addArc(
                        tabCenterX - circleMaxRadius, iconCenterY2 - circleMaxRadius,
                        tabCenterX + circleMaxRadius, iconCenterY2 + circleMaxRadius, 180f, 180f
                    )
                    //tabCenter每一个item的底部的中心
                    circlePath.cubicTo(
                        tabCenterX + circleMaxRadius,
                        iconCenterY2 + circleMaxRadius * C,
                        tabCenterX + circleMaxRadius * C,
                        iconCenterY2 + circleMaxRadius + deltaR,
                        tabCenterX,
                        iconCenterY2 + circleMaxRadius + deltaR
                    )


                    circlePath.cubicTo(
                        tabCenterX - circleMaxRadius * C,
                        iconCenterY2 + circleMaxRadius + deltaR,
                        tabCenterX - circleMaxRadius,
                        iconCenterY2 + circleMaxRadius * C,
                        tabCenterX - circleMaxRadius,
                        iconCenterY2
                    )


                    canvas.drawPath(circlePath, paint)
                    canvas.drawBitmap(iconBitmaps[i]!!, iconLeft, iconTop2, null)
                }
                canvas.drawText(titles[i], tabCenterX, tabTxtBaseLine, paint)
            } else {
                paint.color = Color.WHITE
                canvas.drawText(titles[i], tabCenterX, tabTxtBaseLine, paint)
                canvas.drawBitmap(iconBitmaps[i]!!, iconLeft, iconTop, null)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val x = event.x
            val unitWidth = width * 1.0f / iconRes.size
            val index = (x / unitWidth).toInt()
            if (index == selectIndex) return false
            selectIndex = index
            listen?.invoke(iconRes.get(selectIndex))
            if (valueAnimator != null && valueAnimator!!.isRunning) {
                valueAnimator!!.cancel()
                valueAnimator = null
            }
            valueAnimator = ValueAnimator.ofFloat(0.0f, 2.0f)
            valueAnimator?.duration = 1000
            valueAnimator?.interpolator =BounceInterpolator()
            valueAnimator?.addUpdateListener(AnimatorUpdateListener { animation ->
                animationValue = animation.animatedValue as Float
                invalidate()
            })
            valueAnimator?.start()
        }
        return true
    }

    companion object {
        private const val C = 0.55194f //这个常数可以用贝塞尔来绘制不规则圆形以及规则圆形
    }

    init {
        iconBitmaps = arrayOfNulls(iconRes.size)
        for (i in iconRes.indices) {
            iconBitmaps[i] = BitmapFactory.decodeResource(resources, iconRes[i])
        }
        iconHeight = iconBitmaps[0]!!.height
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.textAlign = Paint.Align.CENTER
        paint.style = Paint.Style.FILL
        paint.textSize = sp2px(12)
        paint.getFontMetrics(fontMetrics)
        padding = dp2px(8)
    }

    //数据回调的接口
    var listen: ((Int) -> Unit)? = null
    fun setCallback(listen:(view:Int)->Unit){
        this.listen = listen
    }
}