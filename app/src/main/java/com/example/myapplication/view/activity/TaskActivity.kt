package com.example.myapplication.view.activity



import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.example.myapplication.R
import com.example.myapplication.adapter.RotateYTransformer
import com.example.myapplication.adapter.VpAdapter
import com.example.myapplication.base.BaseActivity
import com.example.myapplication.model.DataBase
import com.example.myapplication.model.TaskEntity
import com.example.myapplication.view.fragment.Taskt
import kotlinx.android.synthetic.main.activity_task.*
import kotlinx.android.synthetic.main.fragment_task.*


class TaskActivity : BaseActivity() {



    override fun getLayoutId()=R.layout.activity_task


    override fun initData() {
        val intent = intent
        val postion = intent.getIntExtra("postion",0)
        Log.i("点击","拿到的位置值"+postion)
        val  finshlist = DataBase.getDBInstace(this).getTaskDao().getneedtaks(true,false) as MutableList<TaskEntity>
        val nofinshlist = DataBase.getDBInstace(this).getTaskDao().getneedtaks(false,false) as MutableList<TaskEntity>
        finshlist.forEach {
            nofinshlist.add(it)
        }
       // Log.i("点击","值的大小"+            nofinshlist.size)
        val fragmentlist = mutableListOf<Fragment>()
        nofinshlist.forEach {
                val fragement = Taskt(it)
                fragmentlist.add(fragement)
            }
        cardViewPager.setOffscreenPageLimit(3)

           cardViewPager.adapter = VpAdapter(fragmentlist,supportFragmentManager,
               FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
           )
        cardViewPager.setPageTransformer(true,RotateYTransformer())
        cardViewPager.setCurrentItem(postion)
    }


    override fun initListener() {

        back.setOnClickListener {
            finish()
        }




    }

}