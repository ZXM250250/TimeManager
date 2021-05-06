package com.example.requestnetwork


/**
 * 这是一个注解类
 */
@Target(AnnotationTarget.FUNCTION)  //作用在方法上    //get请求
@Retention(AnnotationRetention.RUNTIME) //运行时阶段
annotation class GET(val value:String)




@Target(AnnotationTarget.FUNCTION)  //post请求
@Retention(AnnotationRetention.RUNTIME)
annotation class POST(val value:String)




@Target(AnnotationTarget.FIELD)    //作用于参数  用于多变的网络请求
@Retention(AnnotationRetention.RUNTIME)
annotation class path(val value:String)

@Target(AnnotationTarget.VALUE_PARAMETER)        // 配置键值对信息
@Retention(AnnotationRetention.RUNTIME)
annotation class Field()