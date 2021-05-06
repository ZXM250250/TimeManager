package com.example.requestnetwork

import android.os.Handler
import android.util.Log
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.LinkedBlockingQueue

/**
 * 执行网络请求的类
 */
 class NetworkRequest<T>(val request: Request):Thread() {    //执行网络请求的类


    private val handler: Handler = Handler {   //
        false
    }

    //将流转为字符串的方法
    fun StreamToString(inputStream: InputStream): String {
        val sb = StringBuilder()
        var oneLine:String?= null
        val reader = BufferedReader(InputStreamReader(inputStream))
        oneLine = reader.readLine()
        while (oneLine!= null) {
            sb.append(oneLine).append('\n')
            oneLine = reader.readLine()
        }
        inputStream.close()
        return sb.toString()
    }

    override fun run() {

//            if (request.tag.equals("GET")) {    //get的请求方案
                val url = URL(request.url)
        Log.i("测试","网络请求的url"+request.url)
                val connection = url.openConnection() as HttpURLConnection
                connection.apply {
                    connection.requestMethod = "GET"
                    this.connectTimeout = 8000
                    this.readTimeout = 80000
                }
                //  Log.i("测试", "拿到流之前")
                val input = connection.inputStream
                val str = StreamToString(input)
                Log.i("测试","返回的数据是空的吗"+str)
                // Log.i("测试", "拿到流之后" + request.MyGson)
                connection.disconnect()
                if (request.MyGson != null) {  //需要返回对象
                  //  val jsonData = "{\"school\":{\"name\":\"某中学\",\"presidnet\": {\"name\":\"王兮\",\"salary\":100000000,\"age\":30},\"student\": [{\"name\":\"A\",\"age\":19},{\"name\":\"B\",\"age\":19}, {\"name\":\"C\",\"age\":18}]}}"
                    val clazz = request.MyGson!!.fromJsontoobject(str, request.type as Class<Any>)
                    //考虑返回对象的操作presidnet
                    //  Log.i("测试","返回的东西dd"+clazz)
                    //   Log.i("测试", "返回的东西线程前dd" + Thread.currentThread())
                    // Log.i("测试", "返回的东西线程ddxxcsc")

                    handler.post {
                        //  Log.i("开局","返回的东西线程ddxx")
                        //  Log.i("测试","负的"+listen)
                        listen?.invoke(clazz as Any)
                       //listen =null
                        this.interrupt()
                    }

                }else{     //没对象转换器的时候就直接返回字符串
                    handler.post{
                            Log.i("测试","有拿到返回的数据吗"+str)
                        listen?.invoke(str)
                      //listen = null
                    }
                    this.interrupt()
                   // complete.invoke()
                }
//            }else{   //post的请求方案
//                val url = URL(request.baseUrl)
//                val  httpURLConnection = url.openConnection() as HttpURLConnection
//                httpURLConnection.also {
//                    it.requestMethod = "POST"
//                    it.readTimeout=8000
//                    it.connectTimeout=8000
//                }
//                val dataTowrite = StringBuilder()




        }


            //数据回调的接口
     var listen: ((Any) -> Unit)? = null


    /**
     * 这个方法 很管关键  这里就是留给用户去调用的   并且把最终的结果
     * 给返回去
     */
    fun setCallback(listen:(clazz:Any)->Unit){
        this.listen = listen
        Execute.startAllRequestThread(this)
    }
                //完成线程回调接口
//    /**
//     * 这一个结束 主要完成的是一些资源的回收操作
//     * 比如关闭线程
//     */
//    lateinit   var complete:() -> Unit
//
//
//     fun  setcompletelisten(listen: () -> Unit){
//         complete = listen
//     }

}
