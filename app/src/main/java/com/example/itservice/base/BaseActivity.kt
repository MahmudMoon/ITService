package com.example.itservice.base

import androidx.appcompat.app.AppCompatActivity
import com.example.itservice.R

open class BaseActivity : AppCompatActivity() {
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_fade_in, R.anim.slide_in_left)
    }

    fun moveWithAnimationToAnotherActivity(){
        overridePendingTransition(R.anim.slide_fade_in, R.anim.slide_in_right)
    }
}