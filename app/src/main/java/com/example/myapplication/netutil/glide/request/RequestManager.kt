package com.example.myglide.request

import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue


object RequestManager {      //负责所有的请求管理
    private  val bitmapDispatchers:MutableList<PicDispatcherThread> = mutableListOf<PicDispatcherThread>()
    private val requestQueue by lazy {   //网络请求的阻塞队列
        LinkedBlockingQueue<BitmapRequest>();
    }

    val executorService by lazy {    //线程池管理
            Executors.newScheduledThreadPool(4)
    }

    fun addBitmapRequest(bitmapRequest: BitmapRequest) {     //传过来进行网络请求
        if (!requestQueue.contains(bitmapRequest)){        //如果队列没有加加入
                requestQueue.put(bitmapRequest)
        }
        startAllPicDispatcherThread()
    }


    // 处理并开始所有的线程
    fun startAllPicDispatcherThread(){
        PicDispatcherThread.setListen {
            stop()
        }
        // 获取线程最大数量
            val threadCount = Runtime.getRuntime().availableProcessors()
            if (threadCount>0){
                for (index in 1..threadCount){
                val picDispatcherThread = PicDispatcherThread(requestQueue)
                    executorService.execute(picDispatcherThread)
                //    Log.i("测试","执行了吗startAllPicDispatcherThread()")
                    bitmapDispatchers.add(picDispatcherThread)
                }
            }
    }


    fun stop(){   //停止所有线程
        for (i in bitmapDispatchers){
            i.interrupt()
            bitmapDispatchers.remove(i)
        }
    }


}












