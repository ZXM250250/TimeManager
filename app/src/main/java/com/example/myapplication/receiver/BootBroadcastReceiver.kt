package com.example.myapplication.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.myapplication.service.RemindService

class BootBroadcastReceiver: BroadcastReceiver() {



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
      //  Log.i("嘿嘿","我keyi1街道打撒大厦内的")
            val service = Intent(context,RemindService::class.java)
        context!!.startForegroundService(service)
        val intent: Intent? =context!!.getPackageManager().getLaunchIntentForPackage(context.packageName)
        context.startActivity(intent)

    }
}