package com.example.itservice.common.service_pack.add_catagories.add_brand.add_service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itservice.common.models.DbUpdateResult
import com.example.itservice.common.models.Service
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.google.firebase.database.DatabaseReference

class AddServiceViewModel: ViewModel() {
    var uploadPhoto: MutableLiveData<String> = MutableLiveData()

    private var _serviceDataAdded: MutableLiveData<DbUpdateResult> = MutableLiveData()
    val serviceDataAdded: LiveData<DbUpdateResult>
        get() = _serviceDataAdded

    fun getRootRef(): DatabaseReference {
        return DbInstance.getDbInstance().reference.child(Constants.SERVICE_CATAGORIES)
    }

    fun getNewKey(): String?{
        val rootRef = getRootRef()
        return rootRef.push().key
    }

    fun storeserviceDataInFirebase(catId: String ,brandId: String ,service: Service?) {
        val rootRef = getRootRef().child(catId).child(Constants.brands).child(brandId).child(Constants.SERVICE_LIST)
        rootRef.child(service?.id!!)
            .setValue(service)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    _serviceDataAdded.postValue(DbUpdateResult(true, null))
                }else{
                    _serviceDataAdded.postValue(DbUpdateResult(false, null))
                }
            }
    }
}