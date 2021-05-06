package com.example.myapplication.view.activity
import android.animation.*
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.core.animation.doOnEnd
import com.example.myapplication.R
import com.example.myapplication.base.BaseActivity
import com.example.myapplication.net.net
import com.example.myapplication.netutil.request.MyGosn
import com.example.myapplication.util.JellyInterpolator
import com.example.requestnetwork.Request
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.input_layout.*
import kotlinx.android.synthetic.main.title_layout.*


/**
 * 登录的Activity  是由自己 写的后端项目
 */
class LoginActivity : BaseActivity() {
    private var mWidth = 0f
    private  var mHeight = 0f
    lateinit var network:net

//    val r = Request()
//            .setbaseurl("https://pixabay.com/api/")
//            .addConverterFactory(MyGosn())
//            .create(Requesttest::class.java) as Requesttest
//    val map = mutableMapOf<String, String>()
//    map.put("key", "12472743-874dc01dadd26dc44e0801d61&q")
    override fun getLayoutId()=R.layout.activity_login
    val prefs by lazy {  getSharedPreferences("data", Context.MODE_PRIVATE)}
    @RequiresApi(Build.VERSION_CODES.O)
    override fun initData() {
        network = Request().setbaseurl("http://39.108.84.46:8080/demo3-1.0-SNAPSHOT")
                .create(net::class.java) as net
    }


    override fun initListener() {
        main_btn_login.setOnClickListener {
            //注册的逻辑
            if (main_btn_login.text.equals("Sign up")){
                //符合注册要求
                if (isBare()){   //可以登录
//
//                    val map =mutableMapOf<String, String>()
//                    map.put("username",account.text.toString())
//                    map.put("password",password.text.toString())
                  val  editor=prefs.edit()
                    Log.e("登录","好好"+account.text.toString()+password.text.toString())
                    editor.putString(account.text.toString(), password.text.toString())
                    editor.commit();
                    Log.e("登录","好好"+prefs.getString(account.text.toString(),"没有"))
                //  val s=  network.siginUp(map)
//                    s.setCallback {
                      toast("恭喜你注册成功")
                      main_btn_login.text = "Login"
//                    }
                }else{  //不符合注册要求
                 toast("账号和密码不可以为空哦！！！")
                }
            }else{  //登录的逻辑
                    if (isBare()){  //可以登录
                       // val map =mutableMapOf<String, String>()
                       // map.put("username",account.text.toString())
                      //  map.put("password",password.text.toString())
                       // val s  =network.login(map)
                    //    s.setCallback {
                        Log.e("登录","ziji账号"+account.text.toString()+"密码"+password.text.toString())
                        Log.e("登录","储存"+prefs.getString(account.text.toString(),""))
                        if (prefs.getString(account.text.toString(),"").equals(password.text.toString())){
                            mWidth = main_btn_login.measuredWidth.toFloat()
                            mHeight = main_btn_login.measuredHeight.toFloat()
                            input_layout_name.visibility = View.INVISIBLE
                            input_layout_psw.visibility = View.INVISIBLE
                            inputAnimator(input_layout, mWidth, mHeight);

                        }else{
                            toast("账号或者密码错误！！")
                        }


                     //   }


                    }else{//不可以登录
                        toast("账号和密码不可以为空哦！！！")
                    }
            }

        }

        sign_up.setOnClickListener {
          main_btn_login.text = "Sign up"
            toast("欢迎注册你的账号")
        }
    }
     fun isBare() = password.text!=null&&account.text!=null

    /**
     * @param w表示的是按钮的宽度
     * @param h 表示的是按钮的高度
     */
    fun inputAnimator(view:View,w:Float,h:Float){
        val set  = AnimatorSet()
        val animator = ValueAnimator.ofFloat(0f,w)
        animator.addUpdateListener {
            val value = it.getAnimatedValue() as Float
            val params: ViewGroup.MarginLayoutParams = view
                    .layoutParams as ViewGroup.MarginLayoutParams
            params.leftMargin = value.toInt()
            params.rightMargin = value.toInt()
            view.layoutParams = params
        }

        val animator2: ObjectAnimator = ObjectAnimator.ofFloat(input_layout,
                "scaleX", 1f, 0.5f)
        set.duration = 1000
        set.interpolator = AccelerateDecelerateInterpolator()

        set.playTogether(animator2,animator)
        set.start()
        set.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                /**
                 * 动画结束后，先显示加载的动画，然后再隐藏输入框
                 */
                layout_progress.setVisibility(View.VISIBLE)
                input_layout.setVisibility(View.INVISIBLE)
                progressAnimator( layout_progress)
            }

            override fun onAnimationCancel(animation: Animator?) {}
        })

    }

    /**
     * 出现进度动画
     *
     * @param view
     */
    private fun progressAnimator(view: View) {
        val animator: PropertyValuesHolder = PropertyValuesHolder.ofFloat("scaleX",
                0.5f, 1f)
        val animator2: PropertyValuesHolder = PropertyValuesHolder.ofFloat("scaleY",
                0.5f, 1f)
        val animator3 = ObjectAnimator.ofPropertyValuesHolder(view,
                animator, animator2)
        animator3.duration = 1500
        animator3.doOnEnd {
           startActivity(MainActivity())
        }
        animator3.interpolator = JellyInterpolator()
        animator3.start()

    }

//    /**
//     *点击事件发生的时候
//     * 计算出 按钮的宽和高
//     * 并且使得账号和密码的输入框不可见
//     */
//    override fun onClick(v: View) {
//       when(v.id){
//           main_btn_login->{
//               mWidth = main_btn_login.measuredWidth.toFloat()
//               mHeight = main_btn_login.measuredHeight.toFloat()
//               input_layout_name.visibility = View.INVISIBLE
//               input_layout_psw.visibility = View.INVISIBLE
//               inputAnimator(input_layout, mWidth, mHeight);
//           }
//       }
//    }

}