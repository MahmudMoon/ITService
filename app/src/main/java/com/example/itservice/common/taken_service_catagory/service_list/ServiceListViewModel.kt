package com.example.itservice.common.taken_service_catagory.service_list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itservice.application.TAG
import com.example.itservice.common.models.ServicesTaken
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class ServiceListViewModel: ViewModel() {

    private val listOfServices = ArrayList<ServicesTaken>()
    private var _allServiceData = MutableLiveData<List<ServicesTaken>>()
    val allServiceData : LiveData<List<ServicesTaken>>
        get() = _allServiceData


    private val pendingTaskList = ArrayList<ServicesTaken>()
    private val assignedTaskList = ArrayList<ServicesTaken>()
    private val completedTaskList = ArrayList<ServicesTaken>()


    private val serviceTakenListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            listOfServices.clear()
            pendingTaskList.clear()
            assignedTaskList.clear()
            completedTaskList.clear()
            snapshot.children.forEach { firstChild ->
                val takenService = firstChild.getValue<ServicesTaken>()
                Log.d(TAG, "onDataChange: ${takenService?.serviceID}")
                Log.d(TAG, "onDataChange: ${takenService?.serviceID}")
                if(takenService?.status == Constants.ServiceStatus.Pending.name){
                    pendingTaskList.add(takenService)
                }else if(takenService?.status == Constants.ServiceStatus.Assigned.name){
                    assignedTaskList.add(takenService)
                }else if(takenService?.status == Constants.ServiceStatus.Completed.name){
                    completedTaskList.add(takenService)
                }
                listOfServices.add(takenService!!)
            }
            _allServiceData.postValue(listOfServices)
        }

        override fun onCancelled(error: DatabaseError) {
        }

    }

    private val userUidListListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            listOfServices.clear()
            pendingTaskList.clear()
            assignedTaskList.clear()
            completedTaskList.clear()
            snapshot.children.forEach { firstChild ->
                val takenService = firstChild.getValue<ServicesTaken>()
                Log.d(TAG, "onDataChange: ${takenService?.serviceID}")
                if(takenService?.status == Constants.ServiceStatus.Pending.name){
                    pendingTaskList.add(takenService)
                }else if(takenService?.status == Constants.ServiceStatus.Assigned.name){
                    assignedTaskList.add(takenService)
                }else if(takenService?.status == Constants.ServiceStatus.Completed.name){
                    completedTaskList.add(takenService)
                }
                listOfServices.add(takenService!!)
            }
            _allServiceData.postValue(listOfServices)
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d(TAG, "onCancelled: ${error.message}")
        }

    }

    fun getRootRef(): DatabaseReference {
        return DbInstance.getDbInstance().reference.child(Constants.TakenSericeRoot)
    }

    fun getAllTakenServiceData(uid: String? = null) {
        listOfServices.clear()
        pendingTaskList.clear()
        assignedTaskList.clear()
        completedTaskList.clear()

        var path = getRootRef()
        if(uid != null) {
            getServiceTakenObjcets(path)
        }else {
            path.addValueEventListener(userUidListListener)
        }

    }

    private fun getServiceTakenObjcets(path: DatabaseReference) {
        path.addValueEventListener(serviceTakenListener)
    }

    fun getDataForList(statusType: String?): List<ServicesTaken>? {
        when(statusType){
            Constants.ServiceStatus.Pending.name -> {
                return pendingTaskList
            }

            Constants.ServiceStatus.Assigned.name ->{
                return assignedTaskList
            }

            Constants.ServiceStatus.Completed.name ->{
                return completedTaskList
            }

        }
        return null
    }
}