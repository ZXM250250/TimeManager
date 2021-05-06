package com.example.requestnetwork

import android.util.Log
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

class InvokeMethod(val request: Request,val calzz:Class<*>): InvocationHandler {   //jdk动态代理方法执行的类


    /**
     * 这里主要实现的 是jdk动态的执行方法  在这里就是执行 具体的代码
     * 在这里包括  url的拼接   返回值的类型推断   网络请求的类型（POSt和GET）
     * 并且 在最后的返回值里面  返回的是一个已经封装好的网络请求对象
     *
     *
     * 按照此处的写法   用户所使用的网络 请求是支持一个
     * 参数的   （GET）一个是String类型的  (POST)另一个是map类型的
     * 其目的就是为了 去迎合多变的网络请求
     */
    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): NetworkRequest<*> {
                    //Log.i("测试","这里被回调了吗")
                    //判断请求方式  拼接请求url
                    //线程切换回来 返回值解析
                    //判断返回什么对象
                    //将对象包装返回即可成功
               //   Log.i("测试","得到的返回类型"+method)
                  val analysis= Analysis(calzz,request)
                  request.type=analysis.analysis(method)
       // Log.i("测试","得到的返回类型"+request.type)
                    var s: NetworkRequest<*>? = null
                 if (method?.isAnnotationPresent(GET::class.java)!!){   //发起了get请求
                    // Log.i("开局","发起了get")
                     val urlAnotation = method.getAnnotation(GET::class.java)
                     val u = urlAnotation.value   //附加的url
                     var s = args?.get(0)
                     if (s!=null){
                         request.url= request.getbaseUrl()+u+s
                     }else{
                         request.url = request.getbaseUrl()+u
                     }  //至此得到访问网络的url
                   //  request.tag = "GET"
                     s= Execute.addRequest(request)
                     return s!!
                 }else if (method?.isAnnotationPresent(POST::class.java)!!){  //发起了POST请求
                     val urlAnotation = method.getAnnotation(POST::class.java)
                     val u = urlAnotation.value   //附加的url
                     val dataTowrite = StringBuilder()     //参数的拼接
                     val map = args?.get(0) as HashMap<String,String>
                     map.keys.forEach{
                         dataTowrite.append(it).append("=")
                                 .append(map.get(it)).append("&")
                     }
                     request.url = request.getbaseUrl()+u+dataTowrite.substring(0,dataTowrite.length-1)//最终访问网络的url
                     //request.tag="POST"
                     s= Execute.addRequest(request)
                     return s
                 }


        //Log.i("开局","返回的对象是空的吗"+s)

        return s!!
    }



}