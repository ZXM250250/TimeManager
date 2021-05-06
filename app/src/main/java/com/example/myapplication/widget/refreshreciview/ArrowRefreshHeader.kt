package com.example.myrefershview

import android.animation.ValueAnimator
import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.model.DataBase
import com.example.myapplication.model.TaskEntity
import kotlinx.android.synthetic.main.fragment_task.*
import kotlinx.android.synthetic.main.fragment_task.view.*
import kotlinx.android.synthetic.main.refresh_header.view.*



//这是一个刷新头的类  刷新头的所有操作都在这里    其实就是那个刷新的布局可以自定义
class ArrowRefreshHeader@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): LinearLayout(context, attrs, defStyleAttr),IRefreshHeader {


    //初始化动画
    private val mRotateUpAnim by lazy {
        RotateAnimation(0.0f, -180.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
    }

    private val mRotateDownAnim by lazy {
        RotateAnimation(-180.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    }

            //加载进来刷新头的xml文件
    private val mContentLayout by lazy {
        LayoutInflater.from(context).inflate(R.layout.refresh_header,null,false) as LinearLayout
    }

    private var mMeasuredHeight = 0
    private var mState = 0
    init {

        //初始化刷新头的布局信息
        val layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
         layoutParams.setMargins(0,0,0,0)
         this.layoutParams = layoutParams
      //  setPadding(0,0,0,0)

    //将mContentLayout的LayoutParams高度和宽度设为自动包裹并重新测量
        Log.i("测试er", "addView(mContentLayout, LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0))")
        //把这个自定义的刷新头加入这个布局
       addView(mContentLayout, LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0))
      measure(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        mMeasuredHeight = measuredHeight //获得测量后的高度

        //初始化动画
        mRotateUpAnim.duration = 200
        mRotateUpAnim.fillAfter =true
        mRotateDownAnim.duration =200
        mRotateDownAnim.fillAfter=true

    }
    //刷新完成 状态重置
    override fun onReset() {
        setState(STATE_NORMAL)
    }


    //处于下拉状态
    override fun onPrepare() {
        setState(STATE_RELEASE_TO_REFRESH)
    }

    override fun onRefreshing() {
        setState(STATE_REFRESHING)
    }

    override fun onMove(offSet: Float, sumOffSet: Float) {
        if (getVisibleHeight() > 0 || offSet > 0) {
            setVisibleHeight(offSet.toInt() + getVisibleHeight())
            if (mState <= STATE_RELEASE_TO_REFRESH) { // 未处于刷新状态，更新箭头
                if (getVisibleHeight() > mMeasuredHeight) {
                    onPrepare()
                } else {
                    onReset()
                }
            }
        }
    }


    //结束是判断是否刷新
    override fun onRelease(): Boolean {
        var isOnRefresh = false
        val heigth =getVisibleHeight()
        if (heigth==0){ //not visible.
            // not visible.
            isOnRefresh = false
        }
        if (heigth>mMeasuredHeight&&mState<STATE_REFRESHING){
                setState(STATE_REFRESHING)
            isOnRefresh=true
        }

        //下拉距离足够 去刷新
        if (mState==STATE_REFRESHING&&heigth>mMeasuredHeight){

            smoothScrollTo(mMeasuredHeight)
        }

        //下拉距离不够 弹回去
        if (mState!=STATE_REFRESHING){
            smoothScrollTo(0)
        }
//它的值决定  是否进行刷新
        return isOnRefresh
    }


    //刷新结束调用的方法
    override fun refreshComplete() {
        setState(STATE_DONE) //设置刷新的状态为已完成

       // reset()
        //延迟200ms后复位
        //延迟200ms后复位
        Handler().postDelayed({ reset() }, 200)
    }


    //得到头部的view
    override fun getHeaderView(): View {
        return this
    }


    //得到当前头部view的高度
    override fun getVisibleHeight(): Int {
        val lp =mContentLayout.layoutParams
        return lp.height
    }

//利用动画去移动头部view
    private fun smoothScrollTo(destHeight:Int){
        val animator = ValueAnimator.ofInt(getVisibleHeight(), destHeight)
        animator.setDuration(300).start()
        animator.addUpdateListener { animation -> setVisibleHeight(animation.animatedValue as Int) }
        animator.start()
    }

    /**
     * 设置刷新头部可见的高度
     *
     * @param height
     */
    fun setVisibleHeight(height: Int) {
        var height = height
        if (height < 0) height = 0

        //两句要一起用才可以实时移动
        val lp = mContentLayout.layoutParams as LayoutParams
          lp.height = height
      mContentLayout.layoutParams = lp
    }

    /**
     * 刷新的状态
     *是可以触发移动的
     * @param state
     */
    fun setState(state: Int) {
        //状态没有改变时什么也不做
        if (state == mState) return
        when (state) {
            STATE_NORMAL -> {
                if (mState == STATE_RELEASE_TO_REFRESH) {
                    ivHeaderArrow.startAnimation(mRotateDownAnim)
                }

                if (mState == STATE_REFRESHING) {
                    ivHeaderArrow.clearAnimation()
                }
                tvRefreshStatus.setText("下拉刷新")
            }


            STATE_RELEASE_TO_REFRESH -> {
                ivHeaderArrow.setVisibility(View.VISIBLE)
                refreshProgress.setVisibility(View.INVISIBLE)
                if (mState != STATE_RELEASE_TO_REFRESH) {
                    ivHeaderArrow.clearAnimation()
                    ivHeaderArrow.startAnimation(mRotateUpAnim)
                    tvRefreshStatus.setText("释放立即刷新")
                }
            }


            STATE_REFRESHING -> {
                ivHeaderArrow.clearAnimation()
                ivHeaderArrow.setVisibility(View.INVISIBLE)
                refreshProgress.setVisibility(View.VISIBLE)
                smoothScrollTo(mMeasuredHeight)
                tvRefreshStatus.setText("正在刷新...")
            }
            STATE_DONE -> {
                ivHeaderArrow.setVisibility(View.INVISIBLE)
                refreshProgress.setVisibility(View.INVISIBLE)
                tvRefreshStatus.setText("刷新完成")
            }
            else -> {
            }
        }
        mState = state //保存当前刷新的状态
    }


    //重置刷新的状态
    fun reset() {
        smoothScrollTo(0)
        setState(STATE_NORMAL)

    }
}