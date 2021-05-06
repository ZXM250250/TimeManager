package com.example.myapplication.service

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.Vibrator
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.myapplication.R
import com.example.myapplication.model.DataBase
import java.text.SimpleDateFormat
import java.util.*


class RemindService : Service(), IBinderservice {
         var  mediaPlayer:MediaPlayer?=null
    lateinit var remoteViews: RemoteViews
     lateinit var thread: Thread
    override fun onBind(intent: Intent) = myBinder()

    /**
     * 通知管理器
     */
    private var manager: NotificationManager? = null

    inner class myBinder : Binder() {
        open fun getservice() = this@RemindService
    }



    private val lock = java.lang.Object()

    /**
     * 就在这里不断的对 任务的时间进行检查
     * 并且把最近的  拿出来
     *
     */
    fun updateTimer() {

            while (true) {
                //  Log.i("测试", "当前的线程名字" + Thread.currentThread().name)
                var list = DataBase.getDBInstace(this).getTaskDao().getneedtaks(false, false)
                // Log.i("测试", "重新获取数据" + list.size)
                while (list.size == 0) {
                    lock.wait(1000)
                    // Log.i("测试", "线程还在吗")
                    list = DataBase.getDBInstace(this).getTaskDao().getneedtaks(false, false)
                }

                val s = mutableListOf<Date>()
                list.forEach {
                    s.add(datatoString(it.deadline, "yyyy-MM-dd HH:mm:ss"))
                    Log.i("测试", "list里面的事件" + it.deadline)
                }
                s.forEach {
                    Log.i("测试", "s里面的事件" + it.time)
                }


                Log.i("测试", "开始排序钱")
                s.sortBy { it.time }
//            if (s.size >= 2) {
//                for (i in 0..s.size - 1) {
//                    var j = 0
//                   // Log.i("测试", "外圈排序")
//                    while (j < s.size - 1 - i) {
//                     //   Log.i("测试", "内圈排序")
//                        if (s[j + 1]> s[j]) {
//                            val v = s[j]
//                            s[j] = s[j + 1]
//                            s[j + 1] = v
//                            j++
//                          //  Log.i("测试", "排序" + i + "外" + j)
//                        }
//                        j++
//                    }
//                }
//            }
                Log.i("测试", "排序完成")
                s.forEach {
                    Log.i("测试", "排序后的时间" + it.time)
                }

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
                // list.forEach { it.deadline.replace("", " ") }

                //Log.i("测试", "数据的大小" + list.size)
                // val t = list[0].deadline     //.replace(" ", "").replace("\n", " ")
                // Log.i("测试", "这是啥" + t)
                //  Log.i("测试", "系统时间" + text + "图时间" + s[s.size-1].time)
                val systemtime = datatoString(text, "yyyy-MM-dd HH:mm:ss")
                val usertime = s[0].time
                Log.e("测试", "系统Date时间" + systemtime + "我们自己定义的Date" + s[0])
                Log.e("测试", "系统的长时间" + systemtime.time + "我们自己定义的时间" + s[0].time)
                var times = -systemtime.time + s[0].time
                times = -systemtime.time + s[0].time
                Log.i("测试", "相差的毫秒值" + times)

                if (times > 0) {
                    Log.i("测试", "下一次通知的时间" + times)
                    lock.wait(times)
                    if (flage == true) {
                        flage = false
                        continue
                    }
                    //val s = list[0]
                    /**
                     * 在睡够了以后就去发送通知
                     * 就在这里就可以完成通知
                     */
                    //Toast.makeText(this,"成功了",Toast.LENGTH_SHORT).show()
                    val notification = NotificationCompat.Builder(this, "1")
                            .setSmallIcon(R.drawable.clock)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setDefaults(Notification.DEFAULT_SOUND)
                            .setContent(remoteViews)
//                            .setContentText(s.taskName)
//                            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.success))
                            .build()

                    val vibrator = this.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
                    vibrator.vibrate(1000)
                    manager!!.notify(1, notification)
                    if (mediaPlayer != null) {
                        mediaPlayer!!.release()
                        mediaPlayer = null
                    }
                    mediaPlayer = MediaPlayer.create(this, R.raw.didi)
                    mediaPlayer!!.start()

                    /**
                     * 这里就是来保存通知所发出的对象的 位置
                     */
                    Log.i("测试", "去寻找正确的任务" + usertime)
                    var postion = 0
                    for (i in 0..list.size - 1) {
                        Log.i("测试", "寻找正确的任务重" + datatoString(list[i].deadline, "yyyy-MM-dd hh:mm:ss").time)
                        if (usertime.equals(datatoString(list[i].deadline, "yyyy-MM-dd hh:mm:ss").time)) {
                            postion = i
                            break
                        }
                    }
                    Log.i("测试", "去寻找正确的任务" + postion)
                    val entask = list[postion]
                    entask.isfinish = true
                    Log.i("数据库", "数据库的")
                    DataBase.getDBInstace(this).getTaskDao().updatetask(entask)
                    Log.i("测试", "当前响应的数据" + entask.deadline)

                } else {
                    Log.i("超时", "有超时任务过来了")
                    /**
                     * 到了这里的就都说明   设置的时间有问题
                     * 加入未完成的项目当中
                     */
                    var postion = 0
                    for (i in 0..list.size - 1) {
                        if (usertime.equals(datatoString(list[i].deadline, "yyyy-MM-dd HH:mm:ss").time)) {
                            postion = i
                            Log.i("超时", "图的时间" + usertime + "系统自己的时间" + datatoString(list[i].deadline, "yyyy-MM-dd HH:mm:ss").time)
                            break
                        }
                    }
                    val s = list[postion]
                    s.isouttime = true
                    DataBase.getDBInstace(this).getTaskDao().updatetask(s)

                }

            }
        }






    fun datatoString(time: String, format: String): Date {
        val dateFormat = SimpleDateFormat(format)
        val data = dateFormat.parse(time)
        return data

    }

    @SuppressLint("WrongConstant")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {



        return super.onStartCommand(intent,START_STICKY, startId);

    }

    override fun onCreate() {
        super.onCreate()
        remoteViews = RemoteViews(this.packageName, R.layout.removeview)

    thread = Thread{
        synchronized(lock) {
            lock.notify()
            updateTimer()
        }
    }
        thread.start()

        Log.i("测试", "服务的onCreate()")
        val intentFilter = IntentFilter("com.example.myapplication.service.RemindService")
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_USER_PRESENT);
        intentFilter.addAction(Intent.ACTION_BOOT_COMPLETED)
        initNotification()
        registerReceiver(object : BroadcastReceiver() {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onReceive(context: Context?, intent: Intent?) {
                val i = Intent(context,RemindService::class.java)
                context!!.startForegroundService(i)
                Log.i("嘿嘿","收到广播了吗")
                val intent = Intent(context,RemindService::class.java)
                when(intent!!.action.toString()){
                    Intent.ACTION_SCREEN_OFF->{
                        Log.i("嘿嘿","开屏")

                    }
                    Intent.ACTION_SCREEN_ON  ->{
                        Log.i("嘿嘿","开屏")
                        context!!.startService(intent)
                    }
                    Intent.ACTION_USER_PRESENT ->{//解锁
                        Log.i("嘿嘿","CTION_USER_PRESENT")
                        context!!.startService(intent)
                    }
                    Intent.ACTION_BOOT_COMPLETED  ->{
                        context!!.startService(intent)
                        Log.i("嘿嘿","ACTION_BOOT_COMPLETED")
                    }

                }
            }
        }, intentFilter)


        //  Log.i(TAG,"开始播放音乐");

    }

    /**
     * 创建通知渠道
     *
     * @param channelId   渠道id
     * @param channelName 渠道名称
     * @param importance  渠道重要性
     */
    @TargetApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String, importance: Int) {
        val channel = NotificationChannel(channelId, channelName, importance)
        channel.enableLights(false)
        channel.enableVibration(false)
        channel.vibrationPattern = longArrayOf(0)
        channel.setSound(null, null)
        manager = getSystemService(
                NOTIFICATION_SERVICE) as NotificationManager
        manager!!.createNotificationChannel(channel)
    }
//    fun order(list:MutableList<TaskEntity>) {
//        val l = mutableListOf<String>()
//        list.forEach {
//            Log.i("测试",it.deadline)
//            l.add(it.deadline)
//        }
//        list.sortBy { it.deadline }
//        list.forEach {
//            Log.i("测试", it.deadline)
//        }
//
//    }

    private fun initNotification() {
        val channelId = 1
        val channelName = "时间提醒"
        val importance = NotificationManager.IMPORTANCE_LOW
        createNotificationChannel(channelId.toString(), channelName, importance)
        val notification = NotificationCompat.Builder(this, "1")
                .setSmallIcon(R.drawable.woek)
            .setContentText("我可以让你成为时间管理大师")
            .setLargeIcon(BitmapFactory.decodeResource(resources,R.drawable.woek))
            .setContentText("如果可以，请不要干掉我!!")
                .build()
        startForeground(1, notification)

    }
    var flage = false
fun updatethread(){
    synchronized(lock) {
        flage = true
        lock.notify()
    }
}
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDestroy() {
        super.onDestroy()
        val service = Intent(this,RemindService::class.java)
        this.startForegroundService(service)

    }

    override fun noticelistchange() {
        TODO("Not yet implemented")
    }

// inner   class MyReceiver:BroadcastReceiver(){
//
//        override fun onReceive(context: Context?, intent: Intent?) {
//            val action = intent!!.action.toString()
//            when(action){
//                Intent.ACTION_BOOT_COMPLETED->{
//                    Log.i("嘿嘿","在这里执行开机启动")
//                }
//            }
//        }
//
//    }

}