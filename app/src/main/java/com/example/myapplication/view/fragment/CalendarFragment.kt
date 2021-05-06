package com.example.myapplication.view.fragment


import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentPagerAdapter
import com.example.myapplication.R
import com.example.myapplication.adapter.AlphaAndScalePageTransformer
import com.example.myapplication.adapter.Calendarvpdaapter
import com.example.myapplication.base.BaseFragment
import com.example.myapplication.model.DataBase
import kotlinx.android.synthetic.main.fragment_calendar.*


class CalendarFragment : BaseFragment() {
    /**
     * 获取布局view
     */
    override fun initView()= View.inflate(context, R.layout.fragment_calendar,null)

    private val mData = intArrayOf(R.drawable.img0, R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // vp_two.setOffscreenPageLimit(3)
      //  vp_two.setAdapter(FragmentPagerAdapter(context., FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) );
    }

    }































//    var fragm
//    ntlist  = mutableListOf<Tasks>()
//    fun intrfrg(){
//        var fragmentlist  = mutableListOf<Tasks>()
//        val taskEntity = DataBase.getDBInstace(context!!).getTaskDao().getAlltasks()
//        Log.e("碎片","有数据吗"+taskEntity.size)
//        for (i in 0..taskEntity.size-1){
//            val s =Tasks(taskEntity[i])
//            fragmentlist.add(s)
//
//        }
//        val ad = Calendarvpdaapter(fragmentlist,activity!!.supportFragmentManager,
//                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
//        )
//        vp_two.setOffscreenPageLimit(3)
//        vp_two.setPageTransformer(true, AlphaAndScalePageTransformer())
//        vp_two.adapter = ad
//        Log.e("现在","来了来饿了"+ vp_two)
//    }


