package com.example.itservice.common.taken_service_catagory.service_list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itservice.R
import com.example.itservice.application.TAG
import com.example.itservice.base.BaseActivity
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.common.models.ServicesTaken
import com.example.itservice.common.taken_service_catagory.TakenServiceCatagoryViewModel
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.example.itservice.databinding.ActivityServiceListBinding

interface iServiceListInterface{
    fun onServiceSelected()
}

class ServiceListActivity : BaseActivity(), iServiceListInterface {
    private lateinit var binding: ActivityServiceListBinding
    private lateinit var viewModel: ServiceListViewModel
    private lateinit var statusType: String
    private var aryList: ArrayList<ServicesTaken>? = null
    private lateinit var rvDisplayServiceList: RecyclerView
    private lateinit var rvAdapter: ServiceListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitleForActivity("Services")
        binding = ActivityServiceListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        aryList = ArrayList()
        rvDisplayServiceList = binding.rvServiceList
        rvAdapter = ServiceListAdapter(this@ServiceListActivity, aryList!!)
        rvDisplayServiceList.layoutManager = LinearLayoutManager(this)
        rvDisplayServiceList.adapter = rvAdapter

        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(ServiceListViewModel::class.java)

        if(DbInstance.getLastLoginAs(this@ServiceListActivity).equals(Constants.user)){
            val uid = DbInstance.getUserUid(this@ServiceListActivity)
            viewModel.getAllTakenServiceData(uid)
        }else{
            // for admin and engineer
            viewModel.getAllTakenServiceData()
        }

        val bundle = intent?.extras
        if(bundle!=null){
            statusType = bundle.getString(Constants.statusType, "")
        }

        viewModel.allServiceData.observe(this){
            Log.d(TAG, "onCreate: ${it.size}")
            val listPending = viewModel.getDataForList(Constants.ServiceStatus.Pending.name)
            val listAssigned = viewModel.getDataForList(Constants.ServiceStatus.Assigned.name)
            val listCompleted = viewModel.getDataForList(Constants.ServiceStatus.Completed.name)
            aryList?.clear()
            if(DbInstance.getLastLoginAs(this@ServiceListActivity).equals(Constants.user)){
                val uid = DbInstance.getUserUid(this@ServiceListActivity)
               listPending?.forEach { item ->
                   if(item.createdByID == uid){
                       aryList?.add(item)
                   }
               }
                listAssigned?.forEach { item ->
                    if(item.createdByID == uid){
                        aryList?.add(item)
                    }
                }
                listCompleted?.forEach { item ->
                    if(item.createdByID == uid){
                        aryList?.add(item)
                    }
                }
            }else if(DbInstance.getLastLoginAs(this@ServiceListActivity).equals(Constants.engineer)) {
                val uid = DbInstance.getUserUid(this@ServiceListActivity)
                if(statusType == Constants.ServiceStatus.Assigned.name)
                    listAssigned?.forEach { item ->
                    if(item.assignedEngineerID == uid){
                        aryList?.add(item)
                    }
                 }

                if(statusType == Constants.ServiceStatus.Completed.name)
                listCompleted?.forEach { item ->
                    if(item.assignedEngineerID == uid){
                        aryList?.add(item)
                    }
                }
            } else if(DbInstance.getLastLoginAs(this@ServiceListActivity).equals(Constants.admin)){
                if(statusType == Constants.ServiceStatus.Pending.name)
                listPending?.forEach { item ->
                    aryList?.add(item)
                }
                if(statusType == Constants.ServiceStatus.Assigned.name)
                listAssigned?.forEach { item ->
                    aryList?.add(item)
                }
                if(statusType == Constants.ServiceStatus.Completed.name)
                listCompleted?.forEach { item ->
                    aryList?.add(item)
                }
            }

            rvAdapter.notifyDataSetChanged()
        }

    }

    override fun onServiceSelected() {
        moveWithAnimationToAnotherActivity()
    }
}