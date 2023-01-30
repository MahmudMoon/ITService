package com.example.itservice.base

import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.itservice.R

open class BaseActivity : AppCompatActivity() {

    fun setTitleForActivity(title: String){
        supportActionBar?.title = title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_fade_in, R.anim.slide_in_left)
    }

    fun moveWithAnimationToAnotherActivity(){
        overridePendingTransition(R.anim.slide_fade_in, R.anim.slide_in_right)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}