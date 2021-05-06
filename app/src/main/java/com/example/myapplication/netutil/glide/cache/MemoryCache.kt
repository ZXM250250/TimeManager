package com.example.myglide.cache

import android.graphics.Bitmap
import android.util.Log
import androidx.collection.LruCache
import java.lang.ref.SoftReference


//内存缓存
object MemoryCache: LruCache<String, Bitmap>( //一般设置内存大小占系统内存的1/8
        (java.lang.Runtime.getRuntime().maxMemory() / 8).toInt()
) {

     var  softReference:HashMap<String,SoftReference<Bitmap>> =HashMap<String,SoftReference<Bitmap>>()

    // 当有图片从强引用中移除时，将其放进软引用集合中
    override fun entryRemoved(evicted: Boolean, key: String, oldValue: Bitmap, newValue: Bitmap?) {
        if (oldValue != null) {  //移除强引用的再加入软引用
             softReference.put(key, SoftReference(oldValue));
        }
    }


    //获取图片的大小
    override fun sizeOf(key: String, value: Bitmap): Int {
        return value.getRowBytes() * value.getHeight();
    }

    //从内存中去获取图片
    fun getBitmapFromMemory(url:String): Bitmap? {
     //   Log.i("测试二","内存缓存一级每一次进来的url"+url)

       var bitmap = get(url);
      //  Log.i("测试二","这里有图片吗"+bitmap)
        if (bitmap!=null){
         //   Log.i("测试二","内存缓存拿回去的图片"+bitmap)
            return bitmap
        }else{//强引用当中没有的时候 去弱引用找找看
           // Log.i("测试二","强引用当中没有的时候 去弱引用找找看"+bitmap+"下面的条件可以进入吗"+softReference.size)
            if (softReference.size>0){
               // Log.i("测试二","softReference?.get(url) as Bitmap"+bitmap)
                /**
                 * 经过测试  这个softReference.get(url) as Bitmap 取软引用的方法 存在问题
                 * 程序并不往下执行   推测是被堵塞   尚不能解决问题
                 */
                //  bitmap = softReference.get(url) as Bitmap

                //Log.i("测试二","弱引用拿到图片了吗"+bitmap)
                if (bitmap != null) {
                    //从内存软引用中获取图片
                    this.put(url, bitmap);
                    //并且加入强引用
                  //  Log.i("测试二","内存缓存拿回去的图片"+bitmap)
                    return bitmap;
                }
            }
        }
      //  Log.i("测试二","到底拿到图片了吗"+bitmap)
        return null
    }



}


