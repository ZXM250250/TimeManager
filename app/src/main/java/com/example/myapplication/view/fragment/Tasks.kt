package com.example.myapplication.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.base.BaseFragment
import com.example.myapplication.model.TaskEntity
import kotlinx.android.synthetic.main.taksview.*

class Tasks(val taskEntity: TaskEntity) : BaseFragment() {
    /**
     * 获取布局view
     */
   override  fun initView()= View.inflate(context,R.layout.taksview,null)


    override fun onResume() {
        onCreate(null)
        super.onResume()
          initView()
        Log.e("现在","存在吗dasdsadsaf")
        task_head.text = taskEntity.taskName
        descrip.text = taskEntity.description
        Log.e("现在","存在吗"+task_head.text)

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }




}