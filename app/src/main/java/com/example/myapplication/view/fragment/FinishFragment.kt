package com.example.myapplication.view.fragment

import android.graphics.Color
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R

import com.example.myapplication.base.BaseFragment
import com.example.myapplication.model.TaskEntity
import kotlinx.android.synthetic.main.fragment_task.*

class FinishFragment :BaseFragment() {

    /**
     * 获取布局view
     */
    override fun initView()= View.inflate(context,R.layout.fragment_finish,null)


    override fun initListener() {

    }

    override fun initData() {


    }

}