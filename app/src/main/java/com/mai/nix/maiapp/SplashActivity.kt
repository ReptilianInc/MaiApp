package com.mai.nix.maiapp

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatDelegate
import com.mai.nix.maiapp.choose_groups.NewChooseGroupActivity
import com.mai.nix.maiapp.helpers.UserSettings
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

class SplashActivity : AppCompatActivity() {
    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UserSettings.initialize(this)
        AppCompatDelegate.setDefaultNightMode(UserSettings.getTheme(this))
        setContentView(R.layout.activity_splash)
        startAnimation()
    }

    @ExperimentalCoroutinesApi
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

    @ExperimentalCoroutinesApi
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

    @ExperimentalCoroutinesApi
    private fun launchApp() {
        val intent = if (UserSettings.getGroup(this@SplashActivity) == null) {
            NewChooseGroupActivity.newIntent(this@SplashActivity, false)
        } else {
            Intent(this@SplashActivity, MainActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}