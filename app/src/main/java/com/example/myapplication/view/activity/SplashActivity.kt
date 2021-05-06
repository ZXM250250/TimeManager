package com.example.myapplication.view.activity
import android.content.Intent
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorListener
import com.example.myapplication.R
import com.example.myapplication.base.BaseActivity
import kotlinx.android.synthetic.main.activity_splash.*


/**
 * ClassName:SplashActivity
 * Description:
 */
class SplashActivity: BaseActivity(), ViewPropertyAnimatorListener {
    override fun onAnimationEnd(view: View?) {
        //进入主界面
        startActivity(LoginActivity())
    }


    override fun onAnimationCancel(view: View?) {
    }


    override fun onAnimationStart(view: View?) {
    }

    override fun getLayoutId()=R.layout.activity_splash

    override fun initData() {
        ViewCompat.animate(imageView).scaleX(1.0f).scaleY(1.0f).setListener(this).setDuration(2000)
    }
}