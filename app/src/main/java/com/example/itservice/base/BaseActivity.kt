package com.example.itservice.base

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.itservice.R
import com.example.itservice.admin.admin_dashboard.AdminDashBoardActivity
import com.example.itservice.application.TAG
import com.example.itservice.common.LoginActivity
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.example.itservice.engineer.dashboard.EngineerDashBoardActivity
import com.example.itservice.local_db.DatabaseInstance
import com.example.itservice.local_db.DbHelper
import com.example.itservice.user.user_dash_board.UserdashboardActivity
import kotlin.coroutines.Continuation

interface initBaseViewModel{
    fun initViewModel()
}
open class BaseActivity : AppCompatActivity(), initBaseViewModel {
    private lateinit var viewModel: BaseViewModel
    private lateinit var baseDBHelper: DbHelper

    fun setTitleForActivity(title: String){
        initViewModel()
        supportActionBar?.title = title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    fun onlyChangetitle(title: String){
        supportActionBar?.title = title
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
        }else if(item.itemId == R.id.menu_item_home || item.itemId == R.id.menu_item_home_eng_admin){
            if(DbInstance.getLastLoginAs(this).equals(Constants.user)) {
                startActivity(Intent(this, UserdashboardActivity::class.java))
            }else if(DbInstance.getLastLoginAs(this).equals(Constants.engineer)){
                startActivity(Intent(this, EngineerDashBoardActivity::class.java))
            }else if(DbInstance.getLastLoginAs(this).equals(Constants.admin)){
                startActivity(Intent(this, AdminDashBoardActivity::class.java))
            }
            moveWithAnimationToAnotherActivity()
        }else if(item.itemId == R.id.menu_item_profile){

        }else if(item.itemId == R.id.menu_item_logout_eng_admin || item.itemId == R.id.menu_item_logout){
            baseDBHelper = DatabaseInstance.getDatabaseReference(this)
            if(DbInstance.getLastLoginAs(this).equals(Constants.user)) viewModel.clearDb(baseDBHelper)
            viewModel.logoutuser()
            startActivity(Intent(this, LoginActivity::class.java))
            moveWithAnimationToAnotherActivity()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(DbInstance.getLastLoginAs(this).equals(Constants.admin))
            menuInflater.inflate(R.menu.dash_board_menu_for_admin_eng, menu)
        else if(DbInstance.getLastLoginAs(this).equals(Constants.engineer)){
            menuInflater.inflate(R.menu.dash_board_menu_for_admin_eng, menu)
        }else{
            menuInflater.inflate(R.menu.dash_board_menu, menu)
        }
        return true
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(BaseViewModel::class.java)
    }
}