package com.mai.nix.maiapp

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewTreeObserver
import kotlinx.android.synthetic.main.activity_new_splash.*

class NewSplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_splash)
        UserSettings.initialize(this)
        startAnimation()
    }

    private fun startAnimation() {
        animateCenterTitle()
        skyView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                animateAirPlanes()
                skyView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    private fun animateCenterTitle() {
        centerTitle.animate()
                .alpha(1.0f)
                .setDuration(2500)
                .setListener(null)
    }

    private fun animateAirPlanes() {
        val objectAnimator = ObjectAnimator
                .ofFloat(planeView, "y", skyView.height.toFloat(), skyView.top.toFloat() - planeView.height)
                .setDuration(3000)
        objectAnimator.start()
        objectAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                launchApp()
            }

            override fun onAnimationCancel(animation: Animator?) {}

            override fun onAnimationRepeat(animation: Animator?) {}
        })
    }

    private fun launchApp() {
        val intent = if (UserSettings.getGroup(this@NewSplashActivity) == null) {
            ChooseGroupActivity.newIntent(this@NewSplashActivity, true)
        } else {
            Intent(this@NewSplashActivity, MainActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}