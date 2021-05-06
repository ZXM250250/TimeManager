package com.example.myglide.request

import android.content.Context
import com.example.myglide.request.BitmapRequest

class MyGlide {     //链式调用者

    companion object{  //静态方法
        fun with(context: Context) = BitmapRequest(context)
    }



}