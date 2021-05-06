package com.example.myglide.cache
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.myglide.request.BitmapRequest
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
object NetCache {

    //网络请求得到照片并且压缩图片
    fun getbitmap(u: String,request: BitmapRequest):Bitmap {
      //  Log.i("图片","网络开始请求")
        var url = URL(u)
        Log.i("测试","网络缓存一级每一次进来的url"+u)
        var urlConnection = url.openConnection() as HttpURLConnection
        urlConnection.let {
            it.requestMethod = "GET"
            it.connectTimeout = 3000
            it.readTimeout = 3000
        }
       // Log.i("图片","当前的线程"+Thread.currentThread().name)
       // Log.i("图片","拿到流之前")
        val input = urlConnection.inputStream
       // Log.i("图片","拿到流之后")
        val data: ByteArray = readStream(input)
        //val bitmap = BitmapFactory.decodeStream(input)
        val bitmap= optimizeBitmap(data, request)
        urlConnection.disconnect()
        return bitmap

    }
    //用于压缩图片
    fun optimizeBitmap(byteArray: ByteArray,request: BitmapRequest):Bitmap{
        val options:BitmapFactory.Options = BitmapFactory.Options()
      //  Log.i("测试采样率","流还存在吗"+options.inSampleSize )
        options.inJustDecodeBounds = true
        BitmapFactory.decodeByteArray(byteArray,0,byteArray.size,options)
        options.inSampleSize = calculateInSampleSize(options, request)
        options.inJustDecodeBounds =false
      //  Log.i("测试采样率","流还存在吗"+options.inJustDecodeBounds)
        val bitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.size,options)
      //  Log.i("测试采样率","有图片回来吗"+bitmap.width)
        return bitmap!!

    }

    //计算采样率
    fun calculateInSampleSize(options:BitmapFactory.Options,request: BitmapRequest):Int{
        //得到需要加载的高度和宽度
        val reheight = request.imageView?.height
        val rewidth = request.imageView?.width
      ///  Log.i("测试采样率","图片空了吗"+request.imageView)
       // Log.i("测试采样率","需要的宽高"+reheight+rewidth)
        //得到实际的宽高
        val height = options.outHeight
        val width  = options.outWidth
        //计算采样率
      //  Log.i("测试采样率","实际的"+height+width)
        var inSampleSize = 1
        if(height> reheight!! ||width>rewidth!!){
            val halfHeight = height/2
            val halfWidth = width/2
            while ((halfHeight/inSampleSize)>=reheight
                    &&(halfWidth/inSampleSize)>= rewidth!!){
                inSampleSize*=2
            }
        }
      //  Log.i("测试采样率","值为"+inSampleSize)
        return inSampleSize
    }

    //将流转为字节数组   可以被多次读取  防止读取bitmap对象为空值
    fun readStream(inputStream: InputStream): ByteArray {
        val buffer = ByteArray(1024)
        var len = -1
        val outStream = ByteArrayOutputStream()
        while (inputStream.read(buffer).also({ len = it }) != -1) {
            outStream.write(buffer, 0, len)
        }

        val data: ByteArray = outStream.toByteArray()
        outStream.close()
        inputStream.close()
        return data
    }

}