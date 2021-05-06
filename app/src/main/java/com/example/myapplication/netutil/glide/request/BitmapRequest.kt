package com.example.myglide.request
import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView


class BitmapRequest( val context: Context) {    //解决链式调用

    lateinit var url:String
    private lateinit var bitmap: Bitmap
   var imageView: ImageView? = null
     var load = {url:String->    //得到url
        this.url = url
        this
    }


    var into = {imagive:ImageView->
            imagive.setTag(url)     //把图片和要显示的url一起传过去
        imageView = imagive
        RequestManager.addBitmapRequest(this)
    }




}