package com.example.myapplication.widget

import android.app.TimePickerDialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.graphics.Point
import android.os.IBinder
import android.util.Log
import android.view.*
import android.widget.*
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.example.myapplication.R
import com.example.myapplication.model.TaskEntity
import com.example.myapplication.presenter.impl.TaskPresenterimpl
import com.example.myapplication.presenter.interf.TaskPresneter
import com.example.myapplication.service.RemindService
import com.example.myapplication.view.onViewReseout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class SetTaskPopWindow(val context: Context,val window:Window): PopupWindow(),onViewReseout{

    /**
     * 记录当前窗体的透明度
     */
      var alpha: Float = 0f
      lateinit var view:View
    val fb_yes by lazy {
        view.findViewById<FloatingActionButton>(R.id.fb_yes)
    }


    val fb_cancle by lazy {
        view.findViewById<FloatingActionButton>(R.id.fb_cancle)
    }
    val settime by lazy {
        view.findViewById<TextView>(R.id.tV_settime)
    }
    val TaskPresenterimpl:TaskPresneter by lazy {
        TaskPresenterimpl(context,this)
    }

    val deadtime by lazy {
        view.findViewById<TextView>(R.id.dead_time)
    }
    val taskname by lazy {
        view.findViewById<EditText>(R.id.task_name)
    }
    val remindtime by lazy {
        view.findViewById<TextView>(R.id.remind_time)
    }
    val description by lazy {
        view.findViewById<EditText>(R.id.ed_description)
    }
    val remark by lazy {
        view.findViewById<TextView>(R.id.ed_remark)
    }
    lateinit var remindService:RemindService
    init {
        bindservice()
        //记录当前窗体的透明度
        alpha = window.attributes.alpha
        view = LayoutInflater.from(context).inflate(R.layout.popwndowitem, null, false)
        initlistener()
        contentView = view
        //设置宽度和高度
        width = ViewGroup.LayoutParams.MATCH_PARENT
        val manager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val point = Point()
        manager.defaultDisplay.getSize(point)
        val windowH = point.y
        height = (windowH * 4) / 5
        //设置获取焦点
        isFocusable = true
        //设置外部点击
        isOutsideTouchable = false
        animationStyle = R.style.pop


    }


    override fun showAsDropDown(anchor: View?, xoff: Int, yoff: Int, gravity: Int) {
        val   cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

      val   year = cal.get(Calendar.YEAR).toString()
     val     month =(cal.get(Calendar.MONTH)+1).toString()
    val    day = cal.get(Calendar.DATE).toString()
        settime.text = year+"年"+month+"月"+day+"号"
        super.showAsDropDown(anchor, xoff, yoff, gravity)
        //popwindow已经显示
        val attributes = window.attributes
        attributes.alpha = 0.3f
        //设置到应用程序窗体上
        window.attributes = attributes
    }


    override fun dismiss() {
        super.dismiss()
        //popwindows隐藏 恢复应用程序窗体透明度
        val attributes = window.attributes
        attributes.alpha = alpha
        window.attributes = attributes
    }

    private fun initlistener(){
        var time = ""
        fb_yes.setOnClickListener {
        //    Log.i("测试三","这里来了吗")
            if (deadtime.text.toString().equals("")){
                Toast.makeText(context,"任务的截至日期不要忘了哦！",Toast.LENGTH_SHORT).show()
            }else{
                time = deadtime.text.toString()
               // Log.i("测试","这里执行了吗deadtime.text.toString(")
                if (taskname.text.toString().equals("")){
                    Toast.makeText(context,"任务名称不可以为空哦",Toast.LENGTH_SHORT).show()
                }else{
                    val entity = TaskEntity(taskname.text.toString().replace(" ",""),
                            deadtime.tag.toString(),
                            remindtime.text.toString(),
                            "默认",
                            remark.text.toString(),
                            "默认",
                            description.text.toString(),
                            false,false)
               //     Log.i("测试","存进去的事件"+entity.deadline)
                    TaskPresenterimpl.onmaketask(entity)

                    /**
                     * 去通知服务  数据已经发生改变了
                     */
                   remindService.updatethread()
                    dismiss()
                }
            }


        }
        fb_cancle.setOnClickListener {
            dismiss()
        }
       deadtime.setOnClickListener {
           initTimePicker(it as TextView)

       }
        remindtime.setOnClickListener {
            initTimePicker(it as TextView)
        }

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
                  //      Log.i("时间","dyi"+view.text.toString())
                    //    Log.i("时间","查看小时数"+hourOfDay)
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

//    private String getTime(Date date) {//可根据需要自行截取数据显示
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        return format.format(date);
//    }
//
    /**
     * 成功添加任务的回调
     */
    override fun onSuccess() {
        Toast.makeText(context,"恭喜你！添加任务成功",Toast.LENGTH_SHORT).show()
    }


    fun bindservice(){
        val intent = Intent(context,RemindService::class.java)
        context.bindService(intent,object :ServiceConnection{

            override fun onServiceDisconnected(name: ComponentName?) {

            }


            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val s = service as RemindService.myBinder
                remindService = s.getservice()
            }
        },Context.BIND_AUTO_CREATE)
    }

}