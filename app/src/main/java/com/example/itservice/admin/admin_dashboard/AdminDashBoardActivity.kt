package com.example.itservice.admin.admin_dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import com.example.itservice.R
import com.example.itservice.common.LoginActivity
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.example.itservice.databinding.ActivityAdminDashBoardBinding

class AdminDashBoardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminDashBoardBinding
    private lateinit var viewModel: AdminDashBoradViewModel
    private lateinit var llyPendingTasks: LinearLayout
    private lateinit var llyAssignedTasks: LinearLayout
    private lateinit var llyCompletedTasks: LinearLayout
    private lateinit var llyProductsModify: LinearLayout
    private lateinit var llyServiceModify: LinearLayout
    private lateinit var llyOffersModify: LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDashBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        DbInstance.setLastLoginAs(this@AdminDashBoardActivity, Constants.admin)

        llyAssignedTasks = binding.llyAssignedTasksView
        llyCompletedTasks = binding.llyCompletedTasksView
        llyPendingTasks = binding.llyPendingTasksView
        llyOffersModify = binding.llyEditOffer
        llyProductsModify = binding.llyEditProduct
        llyServiceModify = binding.llyEditService

        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(AdminDashBoradViewModel::class.java)


        llyAssignedTasks.setOnClickListener {

        }
        llyPendingTasks.setOnClickListener {

        }
        llyCompletedTasks.setOnClickListener {

        }
        llyOffersModify.setOnClickListener {

        }
        llyServiceModify.setOnClickListener {

        }
        llyProductsModify.setOnClickListener {

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.dash_board_menu, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_item_logout){
            viewModel.logoutuser()
            finish()
            startActivity(Intent(this@AdminDashBoardActivity, LoginActivity::class.java))
        }
        return true

    }
}