package com.example.myapplication.net

import com.example.requestnetwork.NetworkRequest
import com.example.requestnetwork.POST

interface net {



    @POST("/LoginServlet?")
    fun login(map:Map<String,String>):NetworkRequest<String>

    @POST("/RegisterServlet?")
    fun siginUp(map:Map<String,String>):NetworkRequest<String>
}