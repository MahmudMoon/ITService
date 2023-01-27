package com.example.itservice.common.taken_service_catagory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import com.example.itservice.R
import com.example.itservice.application.TAG
import com.example.itservice.base.BaseActivity
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.common.service_pack.display_service_catagory.DisplayServiceCatagoryActivity
import com.example.itservice.common.taken_service_catagory.service_list.ServiceListActivity
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.example.itservice.databinding.ActivityTakenServiceCatagoryBinding


/* this activity will be used by all type user
1. user
2. admin
3. engineer
 */
class TakenServiceCatagoryActivity : BaseActivity() {
    private lateinit var binding: ActivityTakenServiceCatagoryBinding
    private lateinit var viewModel: TakenServiceCatagoryViewModel
    private lateinit var llyPendingService: LinearLayout
    private lateinit var llyAssignedService: LinearLayout
    private lateinit var llyCompletedService: LinearLayout
    private lateinit var askForService: LinearLayout
    private var isFromAdmin: Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTakenServiceCatagoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(TakenServiceCatagoryViewModel::class.java)

        llyPendingService = binding.llyPendingService
        llyAssignedService = binding.llyAssignedServices
        llyCompletedService = binding.llyCompletedServices
        askForService = binding.llyAskForService

        llyPendingService.setOnClickListener {
            startActivity(Intent(this@TakenServiceCatagoryActivity, ServiceListActivity::class.java)
                .putExtra(Constants.statusType, Constants.ServiceStatus.Pending.name))
        }

        llyAssignedService.setOnClickListener {
            startActivity(Intent(this@TakenServiceCatagoryActivity, ServiceListActivity::class.java)
                .putExtra(Constants.statusType, Constants.ServiceStatus.Assigned.name))
        }

        llyCompletedService.setOnClickListener {
            startActivity(Intent(this@TakenServiceCatagoryActivity, ServiceListActivity::class.java)
                .putExtra(Constants.statusType, Constants.ServiceStatus.Completed.name))
        }

        askForService.setOnClickListener {
            startActivity(Intent(this@TakenServiceCatagoryActivity, DisplayServiceCatagoryActivity::class.java))
        }

    }

    override fun onStart() {
        super.onStart()
        val bundle = intent?.extras
        isFromAdmin = bundle?.getBoolean("fromAdmin", false)
        if(isFromAdmin == true){
            askForService.visibility = View.GONE
        }
    }
}