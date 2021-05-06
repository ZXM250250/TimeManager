package com.example.myapplication.model

import com.example.myapplication.presenter.interf.OnResultListener

interface ITaskModel {

fun  addtask(taskEntity: TaskEntity,onResultListener: OnResultListener)
}