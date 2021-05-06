package com.example.myapplication.adapter

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class Calendarvpdaapter(val fragmentList:List<Fragment>, val  fm: FragmentManager, val  behavior:Int)
    : FragmentPagerAdapter(fm,behavior) {
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return super.isViewFromObject(view, `object`)
    }

    override fun getCount()=fragmentList.size


    override fun getItem(position: Int)=fragmentList.get(position)

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        return super.instantiateItem(container, position)
    }



}