package com.example.myapplication.presenter.impl

import android.content.Context
import com.example.myapplication.model.ITaskModel
import com.example.myapplication.model.ITaskModelimpl
import com.example.myapplication.model.TaskEntity
import com.example.myapplication.presenter.interf.OnResultListener
import com.example.myapplication.presenter.interf.TaskPresneter
import com.example.myapplication.view.onViewReseout

class TaskPresenterimpl(val context: Context,val onViewReseout: onViewReseout):TaskPresneter,OnResultListener {

   val iTaskModel: ITaskModel by lazy {
       ITaskModelimpl(context)
   }



    override fun onmaketask(entity: TaskEntity) {
        iTaskModel.addtask(entity,this)
    }

    override fun onSuccess() {
        onViewReseout.onSuccess()
    }

    override fun onError() {
        TODO("Not yet implemented")
    }


}