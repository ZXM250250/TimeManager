package com.example.myrefershview

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

import com.example.myapplication.model.DataBase
import com.example.myapplication.model.TaskEntity
import com.example.myapplication.view.activity.TaskActivity
import com.example.myapplication.widget.refreshreciview.SlideLayout


//自定义的适配器类  是为了把第一个view改成自定义的头部view
class RefreshHeaderAdapter(val context: Context, val list:MutableList<TaskEntity>, val finssiez:Int): RecyclerView.Adapter<RecyclerView.ViewHolder>(){






    //关于头部刷新的接口
    private lateinit var mRefreshHeader:IRefreshHeader
    class Mvihoulder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var checkBox = itemView.findViewById<CheckBox>(R.id.checkbox)
        var textView = itemView.findViewById<TextView>(R.id.task_content)
        var  mestime = itemView.findViewById<TextView>(R.id.dead_mes)
        var cardview= itemView.findViewById<RelativeLayout>(R.id.card_view)
        val slideview = itemView.findViewById<SlideLayout>(R.id.slideview)
        val  detele = itemView.findViewById<ImageView>(R.id.detele_view)
    }
    class mh(itemView: View):RecyclerView.ViewHolder(itemView)
    class RefreshViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

      return  when(viewType){
           1-> {
               // Log.i("测试er", "onCreateViewHolder返回头布局")
                RefreshViewHolder(mRefreshHeader.getHeaderView())
            }
           2 ->{  RefreshHeaderAdapter.mh(LayoutInflater.from(parent.context)
               .inflate(R.layout.view, parent, false) )   }   //这里是分割
           else->{   RefreshHeaderAdapter.Mvihoulder(LayoutInflater.from(parent.context)
               .inflate(R.layout.itemview, parent, false) )   }   //这里是普通

      }
    }

    override fun getItemCount() = list.size+2   //需要加上一个头部的布局

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var s=0
        if (holder is RefreshHeaderAdapter.Mvihoulder){
            if (position>finssiez){
                s=position-2
            }else{
                s=position-1
            }
            Log.e("报错","list的大小"+list.size+"finssiez"+finssiez+"postion"+position)
            val t =list[s]
           // Log.i("颜色","任务名"+t.taskName+"任务是否完成"+t.isfinish)
            if (t.isfinish){
                holder.cardview.setBackgroundColor(Color.parseColor("#1AE6E6"))
            }else{
                holder.cardview.setBackgroundColor(Color.parseColor("#FAFAFA"))
            }
            holder.slideview.setOnStateChangeListenter(MyOnStateChangeListenter())
            holder.textView.text = t.taskName
            holder.mestime.text = t.deadline
            holder.checkBox.visibility = View.INVISIBLE
            holder.detele.setOnClickListener {

                AlertDialog.Builder(context).apply {
                    setTitle("我想最后提醒你一次")
                    setMessage("数据不可恢复哦")
                    setCancelable(false)
                    setPositiveButton("我意已决") { dialog, which ->
                        DataBase.getDBInstace(context).getTaskDao().deletetask(t)
                        list.remove(t)
                        notifyDataSetChanged()
                    }
                    setNegativeButton("我再好好想想") { dialog, which ->
                        Toast.makeText(context,"还好我没有被抛弃",Toast.LENGTH_SHORT).show()
                    }
                    show()
                }

            }
        }else if (holder is RefreshHeaderAdapter.mh){
            Log.e("报错","分割线list的大小"+list.size+"finssiez"+finssiez+"postion"+position)
        }

        holder.itemView.setOnClickListener {
          //  Log.i("点击","这里有点击事件啊"+s)
            val intent = Intent(context, TaskActivity::class.java)
            intent.putExtra("postion",s)
            context.startActivity(intent)

        }
//            if (position==finssiez-1){
//
//            }else{
//                holder as Mvihoulder
//                val s =list[position]
//                if (s.isfinish){
//                    holder.cardview.setBackgroundColor(Color.parseColor("#1AE6E6"))
//                }else{
//                    holder.cardview.setBackgroundColor(Color.parseColor("#FAFAFA"))
//                }
//                holder.textView.text = s.isfinish.toString()
//                holder.mestime.text = s.deadline
//                holder.checkBox.visibility = View.INVISIBLE
//            }


    }

    override fun getItemViewType(position: Int): Int {

        if (position==0){
            return   1
        }else{
            if (position==finssiez+1){
                return 2
            }else{
                return 3
            }
        }
    }


    fun setRefreshHeader(header: IRefreshHeader){
        mRefreshHeader = header
    }

    private var slideLayout: SlideLayout? = null

    inner class MyOnStateChangeListenter : SlideLayout.OnStateChangeListenter {

        override   fun onClose(layout: SlideLayout?) {
          //  Log.i("哈哈","onClose(")
            if (slideLayout === layout) {
                slideLayout = null
            }
        }

        override  fun onDown(layout: SlideLayout?) {
          //  Log.i("哈哈"," onDown")
            if (slideLayout != null && slideLayout !== layout) {
                slideLayout!!.closeMenu()
            }
        }

        override fun onOpen(layout: SlideLayout?) {

            slideLayout = layout
        }


    }

    internal class ViewHolder {
        var item_content: TextView? = null
        var item_menu: TextView? = null
    }

}