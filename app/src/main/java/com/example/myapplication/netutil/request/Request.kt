package com.example.requestnetwork

import android.os.Build

import androidx.annotation.RequiresApi
import com.example.myapplication.netutil.request.MyGosn
import java.lang.reflect.Type


/**
 * 这一个类的主要作用就是 把一个网络请求
 * 给封装为一个类 一次网络请求
 * 那么就把它封装成一个完整的类
 */
class Request() {    //组装网络请求用的类


    var baseUrl:String? =null
    var MyGson: MyGosn?=null
    var clazz:Any? =null
    var  url:String?=null
   // var tag:String? = null
    var type:Type? = null

    /**
     * 最基本的url
     */
    fun setbaseurl(url:String):Request{
        this.baseUrl=url
        return this
    }


    /**
     * 是否增加  对象的自动转换功能
     */
    fun addConverterFactory(gson:MyGosn):Request{
        this.MyGson=gson
        return this
    }


    /**
     * 完成一个网络请求的构建
     */
    fun build():Request{    //执行构建完成的操作   暂无需求



         return this
    }

    /**
     * 创造一个代理对象  来调用接口中的方法
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun create(clazz:Class<*>):Any?{    //创建类
         this.clazz = clazz
         val analysis = Analysis(clazz,this)
        return analysis.getPoxyClass()
    }

    fun getbaseUrl() = this.baseUrl










}