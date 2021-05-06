package com.example.myapplication.base

import android.content.Intent
import android.os.Bundle
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.util.FullScreen
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.reflect.KProperty0


/**
 * 所有Activity的基类抽取
 */
abstract class BaseActivity() :AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        FullScreen.FullScreen(this)
        initData()
        initListener()
    }
    /**
     * 初始化数据的方法
     */
    open protected fun initData(){

    }

    /**
     * adapter listener
     */

    open protected fun initListener(){

    }

    /**
     * 获取布局id(这一个方法 是为了把活动和视图进行绑定
     * 需要定义为抽象的方法 子类必须要实现这个方法)
     */
    abstract fun getLayoutId():Int

    /**
     * 一个用于Activity跳转的  方法
     */
    fun startActivity(clazz:BaseActivity){
        val intent = Intent(this,clazz.javaClass)
        startActivity(intent)
    }


    fun toast(mesg:String){
        Toast.makeText(this,mesg,Toast.LENGTH_SHORT).show()
    }

  //  public void switchFragment(Activity activity, int id, Fragment fragment){  //留给活动切换碎片
//                  FragmentManager fragmentManager = activity.getFragmentManager();
//                  FragmentTransaction transaction = fragmentManager.beginTransaction();
//                  transaction.add(id,fragment).addToBackStack(null);       // 加入返回栈中
//                  transaction.commit();
//    }

    fun switchfragment(f:Fragment){
        val manager = supportFragmentManager
        val transtion = manager.beginTransaction()
        transtion.replace(R.id.container,f)
        transtion.commit()
    }

}