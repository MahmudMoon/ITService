package com.example.itservice.engineer.dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.example.itservice.R
import com.example.itservice.common.LoginActivity
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.common.taken_service_catagory.service_list.ServiceListActivity
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.example.itservice.databinding.ActivityEngineerDashBoardBinding

class EngineerDashBoardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEngineerDashBoardBinding
    private lateinit var viewModel: EngineerDashBoradViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEngineerDashBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(
            EngineerDashBoradViewModel::class.java)
        DbInstance.setLastLoginAs(this@EngineerDashBoardActivity, Constants.engineer)

        binding.llyAssignedTasksView.setOnClickListener {
            startActivity(Intent(this@EngineerDashBoardActivity, ServiceListActivity::class.java)
                .putExtra(Constants.statusType, Constants.ServiceStatus.Assigned.name))
        }

        binding.llyCompletedTasksView.setOnClickListener {
            startActivity(Intent(this@EngineerDashBoardActivity, ServiceListActivity::class.java)
                .putExtra(Constants.statusType, Constants.ServiceStatus.Completed.name))
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
            startActivity(Intent(this@EngineerDashBoardActivity, LoginActivity::class.java))
        }
        return true
    }
}