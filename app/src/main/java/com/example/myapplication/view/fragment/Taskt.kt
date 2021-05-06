package com.example.myapplication.view.fragment

import android.app.TimePickerDialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.TimePicker
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.example.myapplication.R
import com.example.myapplication.base.BaseFragment
import com.example.myapplication.model.DataBase
import com.example.myapplication.model.TaskEntity
import com.example.myapplication.service.RemindService
import kotlinx.android.synthetic.main.taskview.*
import java.text.SimpleDateFormat
import java.util.*


class Taskt(val taskEntity: TaskEntity) : BaseFragment() {
    /**
     * 获取布局view
     */
    override fun initView()=View.inflate(context, R.layout.taskview,null)
    lateinit var remindService:RemindService


    override fun initData() {
        bindservice()
        dead_time.text = taskEntity.deadline
        remind_time.text  = taskEntity.remindtime
        task_name.text =  SpannableStringBuilder(taskEntity.taskName)
        ed_description.text = SpannableStringBuilder(taskEntity.description)
        ed_remark.text =  SpannableStringBuilder(taskEntity.remark)
        inittime()
    }


    override fun onPause() {
        super.onPause()

     if (task_name.text.toString()!=null){
         taskEntity.taskName = task_name.text.toString()
     }
        if (dead_time.text.toString()!=null){
            taskEntity.deadline = dead_time.text.toString()
        }
        if (remind_time.text.toString()!=null){
            taskEntity.remindtime = remind_time.text.toString()
        }
        if (ed_description.text.toString()!=null){
            taskEntity.description = ed_description.text.toString()
        }

        if (ed_remark.text.toString()!=null){
            taskEntity.remark = ed_remark.text.toString()
        }


//            Log.d("侧滑","这里的数据"+taskEntity.taskName)
//            taskEntity.taskName = task_name.text.toString()
//           taskEntity.deadline = dead_time.text.toString()
//           Log.e("哈哈","这里的数据"+ dead_time.text.toString())
//           Log.e("哈哈","这里的数据"+remind_time.text.toString())
//            taskEntity.description = ed_description.text.toString()
//            taskEntity.remark = ed_remark.text.toString()
            DataBase.getDBInstace(context!!).getTaskDao().updatetask(taskEntity)
            val i = Intent(context,RemindService::class.java)
            context!!.startService(i)
            remindService.updatethread()
            Log.e("哈哈","这里的数据"+taskEntity.taskName)


    }


    override fun initListener() {
        val c = Calendar.getInstance()
        dead_time.setOnClickListener {
         initTimePicker(it as TextView)
        }
        remind_time.setOnClickListener {
            initTimePicker(it as TextView)
        }

    }


    fun inittime(){
        val cal = Calendar.getInstance()
        cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

        val year = cal.get(Calendar.YEAR).toString()
        var month = (cal.get(Calendar.MONTH) + 1).toString()
//            if (month.length<2){
//                month = "0"+month
//            }
        var day = cal.get(Calendar.DATE).toString()
//            if (day.length<2){
//                day = "0"+day
//            }
        var hour = (cal.get(Calendar.HOUR)).toString()
//            if (hour.length<2){
//                hour = "0"+hour
//            }
        var minute = cal.get(Calendar.MINUTE).toString()
//            if (minute.length<2){
//                minute = "0"+minute
//            }
        val second = cal.get(Calendar.SECOND).toString();
        val text = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second
        tV_settime.text = text
    }

    private fun  initTimePicker(view: TextView){
        val pvtime = TimePickerBuilder(context,object : OnTimeSelectListener {
            override fun onTimeSelect(date: Date?, v: View?) {
                view.text = getTime(date!!)
                val c = Calendar.getInstance()
                TimePickerDialog(context,object :TimePickerDialog.OnTimeSetListener{

                    override fun onTimeSet(v: TimePicker?, hourOfDay: Int, minute: Int) {
                        var s = ""
                        if (hourOfDay.toString().length<2){
                            s = "0"+hourOfDay.toString()
                        }else{
                            s = hourOfDay.toString()
                        }
                        var a =""
                        if (minute.toString().length<2){
                            a = "0"+minute.toString()
                        }else{
                            a = minute.toString()
                        }
                   //     Log.i("时间","dyi"+view.text.toString())
                      //  Log.i("时间","查看小时数"+hourOfDay)
                        view.tag = view.text.toString()+" "+s+":"+a+":"+"00"
                        view.text = view.text.toString()+"\n"+"      "+hourOfDay.toString()+":"+minute.toString()
                        Log.i("测试","jdasihd1"+hourOfDay.toString())
                    }

                },c.get(Calendar.HOUR_OF_DAY)
                        ,c.get(Calendar.MINUTE),false).show()
            }
        }).
        isDialog(true)
                .build()
        pvtime.show()
    }
    private fun getTime(date:Date):String{
        val format = SimpleDateFormat("yyyy-MM-dd")
        return format.format(date)
    }


    fun bindservice(){
        val intent = Intent(context, RemindService::class.java)
        context!!.bindService(intent,object : ServiceConnection {

            override fun onServiceDisconnected(name: ComponentName?) {

            }


            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val s = service as RemindService.myBinder
                remindService = s.getservice()
            }
        }, Context.BIND_AUTO_CREATE)
    }
}