package com.example.myrefershview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView


//改装的RecyclerView
class RefreshHeaderRecyclerView@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): RecyclerView(context, attrs, defStyleAttr) {



    //头部
    private lateinit var mRefreshHeader:IRefreshHeader
    //适配器
    private lateinit var mHeaderAdapter:RefreshHeaderAdapter



    private var sumOffSet=0f
    private var mLastY = -1f
    private var mRefreshing = false
    private var mRefreshListener: OnRefreshListener? = null


    fun setAdapter(adapter: RefreshHeaderAdapter){
        mHeaderAdapter = adapter
        //实例化头部
        mRefreshHeader = ArrowRefreshHeader(context)

        //把头部给适配器
        mHeaderAdapter.setRefreshHeader(mRefreshHeader)
        //系统自带设置适配器
        super.setAdapter(adapter)
    }



    override fun onTouchEvent(e: MotionEvent): Boolean {
        if (mLastY==-1f){
            mLastY = e.rawY
        }
        when(e.action){
                MotionEvent.ACTION_DOWN->{
                    mLastY=e.rawY
                    sumOffSet= 0F
                }
            MotionEvent.ACTION_MOVE->{
                val deltay=(e.rawY-mLastY)/2
                mLastY = e.rawY
                sumOffSet+=deltay
                //isOnTop()是否滑到顶部
                if (isOnTop()&&!mRefreshing){
                    mRefreshHeader.onMove(deltay,sumOffSet)
                    if (mRefreshHeader.getVisibleHeight()>0){
                        return false
                    }
                }
            }
            else ->{
                mLastY=-1f
                if (isOnTop()&&!mRefreshing){
                    //手指松开     需要根据当前的位置去判断是否进行刷新
                    if (mRefreshHeader.onRelease()){
                        if (mRefreshing!=null){
                            mRefreshing=true
                            //回调刷新完成的接口
                            mRefreshListener?.onRefresh()
                        }
                    }
                }
            }

        }
        return super.onTouchEvent(e)
    }



    //判断是否滑到顶端
    private fun isOnTop() = mRefreshHeader.getHeaderView().parent!=null




    //刷新完成回调的接口
    fun setOnRefreshListener(listener: OnRefreshListener) {
        mRefreshListener = listener
    }



    //给外部刷新完成时调用的
    fun refreshComplete() {
        if (mRefreshing) {
            mRefreshing = false
            mRefreshHeader.refreshComplete()
        }
    }
}