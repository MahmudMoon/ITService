package com.example.itservice.common.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import androidx.lifecycle.ViewModelProvider
import com.example.itservice.R
import com.example.itservice.application.ITApplication
import com.example.itservice.application.TAG
import com.example.itservice.common.LoginActivity
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.databinding.ActivitySlpashBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySlpashBinding

    private lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySlpashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(SplashViewModel::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            for (i in 0..100 step 5) {
                Log.d(TAG, "onCreate: running in thread + $i")
                // progressBar.progress = i
                delay(250)
            }
            //tmp move to next screen from here
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
        }

        val slideAnimationLeftToRight = AnimationUtils.loadAnimation(this, R.anim.side_slide_left_to_right)
        val slideAnimationRightToLeft = AnimationUtils.loadAnimation(this, R.anim.side_slide_right_to_left)
        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        binding.tvTitle1.startAnimation(slideAnimationLeftToRight)
        binding.tvTitle2.startAnimation(slideAnimationRightToLeft)
        binding.ivIcon.startAnimation(fadeInAnimation)


    }
}