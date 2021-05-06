package com.example.myapplication.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment


/**
 * Fragment的基类抽取
 */
abstract class BaseFragment:Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    /**
     * fragment初始化
     */
   open protected fun init() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = initView()

    /**
     * 获取布局view
     */
    abstract fun initView(): View



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
        initListener()
    }

    
    /**
     * 数据的初始化
     */
   open protected fun initData() {

    }
    /**
     * adapter listener
     */
    open protected fun initListener() {

    }

}