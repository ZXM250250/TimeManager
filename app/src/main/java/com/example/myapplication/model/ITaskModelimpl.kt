package com.example.myapplication.model

import android.content.Context
import com.example.myapplication.presenter.interf.OnResultListener

class ITaskModelimpl(val context: Context):ITaskModel {

    val dataBase by lazy {
        DataBase.getDBInstace(context)
    }
    override fun addtask(taskEntity: TaskEntity, onResultListener: OnResultListener) {
        dataBase.getTaskDao().inserttask(taskEntity)
        onResultListener.onSuccess()
    }




}