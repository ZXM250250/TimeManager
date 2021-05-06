package com.example.myapplication.view.fragment

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R

import com.example.myapplication.base.BaseFragment
import com.example.myapplication.model.DataBase
import com.example.myapplication.model.TaskEntity
import com.example.myapplication.widget.refreshreciview.SlideLayout
import com.example.myrefershview.OnRefreshListener
import com.example.myrefershview.RefreshHeaderAdapter
import kotlinx.android.synthetic.main.fragment_task.*


class TaskFragment : BaseFragment() {


    /**
     * 获取布局view
     */
    override fun initView()=View.inflate(context, R.layout.fragment_task,null)

    override fun initListener() {
        iv_menu.setOnClickListener{
                    draw_view.openDrawer(GravityCompat.START)
        }


        iv_point.setOnClickListener{
            onMenuClick(it)
        }
    }

    override fun initData() {
        Log.e("初始化","碎片启动了吗")
        var  finshlist = DataBase.getDBInstace(context!!).getTaskDao().getneedtaks(true,false) as MutableList<TaskEntity>
        var nofinshlist = DataBase.getDBInstace(context!!).getTaskDao().getneedtaks(false,false) as MutableList<TaskEntity>
        var  size = nofinshlist.size
        Log.e("初始化","完成数据的大小"+finshlist.size)
        finshlist.forEach {
            nofinshlist.add(it)
        }
        Log.e("初始化","总数据大小"+nofinshlist.size)
      //  if (nofinshlist.size>0){
            val layoutManager = LinearLayoutManager(context)
            rv_one.layoutManager = layoutManager
            //rv_one.adapter = adapter
            rv_one.setAdapter( RefreshHeaderAdapter(context!!,nofinshlist,size))
            rv_one.setOnRefreshListener(object :OnRefreshListener{
                override fun onRefresh() {
                    requestData()
                }
            })
     //   }
    }

    @SuppressLint("HandlerLeak")
    private val handler = object : Handler(){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (context!=null){
                Toast.makeText(context, "刷新完成！", Toast.LENGTH_SHORT).show()
                var  finshlist = DataBase.getDBInstace(context!!).getTaskDao().getneedtaks(true,false) as MutableList<TaskEntity>
                var nofinshlist = DataBase.getDBInstace(context!!).getTaskDao().getneedtaks(false,false) as MutableList<TaskEntity>
                var  size = nofinshlist.size
                finshlist.forEach {
                    nofinshlist.add(it)
                }
                rv_one.setAdapter( RefreshHeaderAdapter(context!!,nofinshlist,size))
            }
        }
    }
    fun  requestData(){
        Thread(Runnable {
            try {
                Thread.sleep(1000)
                handler.post {
                    if (rv_one!=null){

                        rv_one.refreshComplete()
                    }
                }
                Thread.sleep(1000)

                rv_one?.refreshComplete()
                val message = Message.obtain()
                message.what = 0
                handler.sendMessage(message)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }).start()
    }

    private fun onMenuClick(view: View) {
        val popup = context?.let { PopupMenu(it, view) }
        popup!!.menuInflater.inflate(R.menu.nav_menu, popup.menu)
        popup!!.setOnMenuItemClickListener {
            onMenuItemClick(it) }
        popup.show()
    }
    private fun onMenuItemClick(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.renewload ->{
                val  finshlist = DataBase.getDBInstace(context!!).getTaskDao().getneedtaks(true,false) as MutableList<TaskEntity>
                val nofinshlist = DataBase.getDBInstace(context!!).getTaskDao().getneedtaks(false,false) as MutableList<TaskEntity>
                val size = nofinshlist.size
                finshlist.forEach {
                    nofinshlist.add(it)
              }
                val list = DataBase.getDBInstace(context!!).getTaskDao().getAlltasks()
//              //  Log.i("数据","数据库有数据吗"+list.size)
//              //  Log.i("数据","数据库的的数据"+list[0].isfinish +list[0].isouttime+list[0].deadline)
//              //  Log.i("数据","adpter有数据吗"+size)
//              for (i in 0..nofinshlist.size-1){
//              //    Log.i("数据","数据的变化"+i)
//              //    Log.i("数据","数据的情况"+nofinshlist[i].isfinish)
            //  }
                //val adapter = TaskAdapter(context!!,nofinshlist,size)
                val layoutManager = LinearLayoutManager(context)
              // rv_one.layoutManager = layoutManager
                rv_one.setAdapter( RefreshHeaderAdapter(context!!,nofinshlist,size))//rv_one.adapter = adapter
                rv_one.adapter!!.notifyDataSetChanged()
                Toast.makeText(context,"更新成功",Toast.LENGTH_SHORT).show()
            }
            R.id.edit-> Toast.makeText(context, "menu2", Toast.LENGTH_SHORT).show()
            R.id.order->{    //排序功能
                val  finshlist = DataBase.getDBInstace(context!!).getTaskDao().getneedtaks(true,false) as MutableList<TaskEntity>
                val nofinshlist = DataBase.getDBInstace(context!!).getTaskDao().getneedtaks(false,false) as MutableList<TaskEntity>
                //Log.i("排序","排序以前")
              //  nofinshlist.forEach {
              //      Log.i("排序","每一条数据"+it.deadline)
             //   }
                nofinshlist.sortBy {
                    it.deadline
                }
              //  Log.i("排序","排序以后")
              //  nofinshlist.forEach {
               //     Log.i("排序","每一条数据"+it.deadline)
             //   }
                val size = nofinshlist.size
                finshlist.forEach {
                    nofinshlist.add(it)
                }
                    DataBase.getDBInstace(context!!).getTaskDao().deteall()
                nofinshlist.forEach {
                    DataBase.getDBInstace(context!!).getTaskDao().inserttask(it)
                }
                //val adapter = TaskAdapter(context!!,nofinshlist,size)
               // val layoutManager = LinearLayoutManager(context)
                // rv_one.layoutManager = layoutManager
                rv_one.setAdapter( RefreshHeaderAdapter(context!!,nofinshlist,size))//rv_one.adapter = adapter
                rv_one.adapter!!.notifyDataSetChanged()
                Toast.makeText(context,"大人看看我排的咋样",Toast.LENGTH_SHORT).show()




            }
            R.id.share -> {  //分享功能

            }
        }
        return false
    }








}

