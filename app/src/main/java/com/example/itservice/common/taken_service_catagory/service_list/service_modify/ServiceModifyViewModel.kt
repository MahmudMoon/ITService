package com.example.itservice.common.taken_service_catagory.service_list.service_modify

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itservice.common.models.DbUpdateResult
import com.example.itservice.common.models.Engineer
import com.example.itservice.common.models.ServicesTaken
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class ServiceModifyViewModel: ViewModel() {
    private var _takenObject = MutableLiveData<ServicesTaken>()
    val takenObject: LiveData<ServicesTaken>
    get() = _takenObject

    private var _dataUpdateCheck = MutableLiveData<DbUpdateResult>()
    val dataUpdateCheck: LiveData<DbUpdateResult>
    get() = _dataUpdateCheck

    private val _engData = MutableLiveData<Engineer>()
    val engData: LiveData<Engineer>
        get() = _engData

    private val engineerDataListener = object: ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            val engineer = snapshot.getValue<Engineer>()
            _engData.postValue(engineer!!)
        }

        override fun onCancelled(error: DatabaseError) {
        }

    }

    private val takenServiceListener = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            val servicesTaken = snapshot.getValue<ServicesTaken>()
            _takenObject.postValue(servicesTaken!!)
        }

        override fun onCancelled(error: DatabaseError) {
        }

    }

    fun getTakenServiceObject(createrId: String?, takeServiceId: String?) {
        DbInstance.getDbInstance().reference.child(Constants.TakenSericeRoot)
            .child(createrId!!)
            .child(takeServiceId!!)
            .addValueEventListener(takenServiceListener)
    }

    fun updateTakenService(takenService: ServicesTaken) {
        DbInstance.getDbInstance().reference.child(Constants.TakenSericeRoot)
            .child(takenService.createdByID!!)
            .child(takenService.id!!)
            .setValue(takenService)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    _dataUpdateCheck.postValue(DbUpdateResult(true, null))
                }else{
                    _dataUpdateCheck.postValue(DbUpdateResult(false, null))
                }
            }
    }

    fun getEngineerDataFromUid(engineerUid: String) {
        DbInstance.getDbInstance().reference.child(Constants.engineer)
            .child(engineerUid)
            .addValueEventListener(engineerDataListener)
    }

}