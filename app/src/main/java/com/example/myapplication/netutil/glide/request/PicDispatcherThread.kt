package com.example.myglide.request
import android.graphics.Bitmap
import android.os.Handler
import com.example.myapplication.util.MD5
import com.example.myglide.cache.LocalCache
import com.example.myglide.cache.MemoryCache
import com.example.myglide.cache.NetCache
import java.util.concurrent.BlockingQueue


class PicDispatcherThread(//得到图片的具体执行者
         var requestQueue:BlockingQueue<BitmapRequest> ):Thread(){


    override fun run() {
        super.run()
        while (!isInterrupted){
        var request = requestQueue?.take() as BitmapRequest
                // 网络加载获取图片资源
           // Log.i("图片测试","线程开始了吗")
               val  bitmap = findBitmap(request);
                // 将图片显示到ImageView
         //   Log.i("图片测试","已经找到图片了")
                showImageView(request,bitmap);
        }
    }


    private fun showImageView(bitmapRequest: BitmapRequest, bitmap: Bitmap?) { //展示图片
                    handler.post{
                        bitmapRequest.imageView?.setImageBitmap(bitmap)
                    }
        if (requestQueue.size==0){
          //  Log.i("测试","到了要停止所有线程")
                listen.invoke()
        }
    }



    companion object{
        private lateinit var listen: () -> Unit  //接口回调  当队列为空的时候 回调
        fun setListen(listen: () -> Unit) {
            Companion.listen =listen
        }
    }

    private val handler: Handler = Handler{   //
        false
    }


    fun findBitmap(request: BitmapRequest):Bitmap?{    //三级储存获得图片的方法
      //  Log.i("图片测试","开始去内存找图片")
        val isfind = MemoryCache.getBitmapFromMemory(MD5.md5(request.url))!=null
      //  Log.i("图片测试","是否能从内存中取到"+isfind)
        if (isfind){   //第一步从内存中去取
            return MemoryCache.getBitmapFromMemory(MD5.md5(request.url))
            }


        //第二次从本地去获取
      ///  Log.i("图片测试","内存没有 要去本地找")
        val img = LocalCache.getBitmapFromLocal(request)
       // Log.i("图片测试","是否在本地找到图片"+img)
        if (img!=null){
            //有的话就加入内存当中
            img.let { MemoryCache.put(MD5.md5(request.url), it) }
            return img
        }
      //  Log.i("图片测试","都没有找到要去网络找")
        //第三次则是发起网络请求 并且加入到内存和本地中
       val bitmap = NetCache.getbitmap(request.url,request)
        //要将得到的图片做优化处理


        if (bitmap != null) {
            MemoryCache.put(MD5.md5(request.url),bitmap)
        }
        if (bitmap != null) {
            LocalCache.setBitmapToLocal(request,bitmap)
        }
       return bitmap

    }


}