package com.example.requestnetwork


import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit


/**
 * 一个单例的线程池类
 * 用来管理所有的线程网络请求
 */
object  Execute {   //管理所有的网络请求

        //将开启的线程放在一起  需要时回收
    private var thread:NetworkRequest<*>?=null
    private val ThreadS:MutableList<NetworkRequest<*>?> = mutableListOf()
    val requestQueue by lazy {   //网络请求的阻塞队列
        LinkedBlockingQueue<NetworkRequest<Any>>()
    }

    val Requests = mutableSetOf<Runnable>()

    val executo =  ThreadPoolExecutor(
            1,
            10,
            60,
            TimeUnit.SECONDS,
            LinkedBlockingQueue()
    )   //创建线程池

    /**
     * @param request  这就是外边的那个网络请求
     */
    fun addRequest(request: Request):NetworkRequest<Any> {
        thread = NetworkRequest<Any>(request)
        requestQueue.add(thread as NetworkRequest<Any>)
//        if (thread!=null){
//            thread=null

//        }else{
//            thread = NetworkRequest<Any>(requestQueue)
//        }

      //  Log.i("开局","返回的对象是空的吗"+ thread)
        //startAllRequestThread()
        return thread as NetworkRequest<Any>
    }

    fun startAllRequestThread(task:Thread){
        executo.execute(task)
                //给线程注册回调接口
                //当队列为空时 停止所有线程
                //获取可以拿到的最大线程数

//        /**
//         * 能有多少线程就去开多少线程
//         * 直到全部完成 所需要的 网络请求的需求
//         */
//        val threadCount = Runtime.getRuntime().availableProcessors()
//        if(threadCount>0){
//            for (index in 1..threadCount){
//                //val thread = requestQueue.take()
//                thread?.setcompletelisten {
//                    stopallThreads()
//                }
//                executo.execute(thread)
//                ThreadS.add(thread)
//            }
//
//        }

    }


//    /**
//     * 停止  所有的线程
//     */
//    fun stopallThreads(){
//        ThreadS.forEach{
//            it?.interrupt()
//            ThreadS.remove(it)
//        }
//    }




}