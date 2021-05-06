package com.example.myapplication.view.activity

import android.content.Intent
import com.example.myapplication.R
import com.example.myapplication.base.BaseActivity
import com.example.myapplication.service.RemindService

import com.example.myapplication.view.fragment.CalendarFragment
import com.example.myapplication.view.fragment.FinishFragment
import com.example.myapplication.view.fragment.TaskFragment
import com.example.myapplication.widget.SetTaskPopWindow
import com.wlj.aiqiyitabviewtest.aiqiyi.TabView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {
    lateinit var tabView: TabView
    override fun getLayoutId() = R.layout.activity_main
    val taskFragment by lazy {
        TaskFragment()
    }
    val finishFragment by lazy {
        FinishFragment()
    }
    val calendarFragment by lazy {
        CalendarFragment()
    }


    override fun initData() {
        val intent = Intent(this,RemindService::class.java)
        startService(intent)
        switchfragment(taskFragment)
        tabView =findViewById(R.id.tabview)

    }

    override fun initListener() {

            tabView.setCallback {
                when(it){
                    R.mipmap.task-> switchfragment(taskFragment)
                    R.mipmap.calendar-> switchfragment(calendarFragment)
                    R.mipmap.vitory-> switchfragment(finishFragment)
                }
            }
        fab.setOnClickListener {
            val popupWindow = SetTaskPopWindow(this,window)
            val bottomh = tabview.height
            popupWindow!!.showAsDropDown(tabview,0,-bottomh)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        val intent = Intent(this,RemindService::class.java)
        stopService(intent)
    }





}
