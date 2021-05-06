package com.example.myrefershview

import android.view.View


//刷新头需要继承的接口   四状态  几个常用方法
interface IRefreshHeader {
    val STATE_NORMAL: Int  //正常状态
        get() = 0

    var STATE_RELEASE_TO_REFRESH: Int  //下拉的状态
        get() = 1
        set(value) = TODO()

    var STATE_REFRESHING: Int  //正在刷新的状态
        get() = 2
        set(value) = TODO()

    var STATE_DONE: Int  //刷新完成的状态
        get() = 3
        set(value) = TODO()


    fun onReset()

    /**
     * 处于可以刷新的状态，已经过了指定距离
     */
    fun onPrepare()

    /**
     * 正在刷新
     */
    fun onRefreshing()

    /**
     * 下拉移动
     */
    fun onMove(offSet: Float, sumOffSet: Float)

    /**
     * 下拉松开
     */
    fun onRelease(): Boolean

    /**
     * 下拉刷新完成
     */
    fun refreshComplete()

    /**
     * 获取HeaderView
     */
    fun getHeaderView(): View

    /**
     * 获取Header的显示高度
     */
    fun getVisibleHeight(): Int
}