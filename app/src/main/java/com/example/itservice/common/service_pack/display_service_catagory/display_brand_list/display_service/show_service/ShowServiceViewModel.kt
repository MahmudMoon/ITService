package com.example.itservice.common.service_pack.display_service_catagory.display_brand_list.display_service.show_service

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itservice.application.TAG
import com.example.itservice.common.models.DbUpdateResult
import com.example.itservice.common.models.Service
import com.example.itservice.common.models.ServicesTaken
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class ShowServiceViewModel: ViewModel() {
    private var _serviceData = MutableLiveData<Service>()
    val serviceData: LiveData<Service>
    get() = _serviceData

    private var _dataAdded = MutableLiveData<DbUpdateResult>()
    val dataAdded: LiveData<DbUpdateResult>
        get() = _dataAdded

    private var _userName = MutableLiveData<String>()
    val userName: LiveData<String>
    get() = _userName

    private var _catName = MutableLiveData<String>()
    val catName: LiveData<String>
        get() = _catName

    private var _brandName = MutableLiveData<String>()
    val brandName: LiveData<String>
        get() = _brandName

    private val stringValueListener = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            val name = snapshot.getValue<String>()
            Log.d(TAG, "onDataChange: userNAme: ${name}")
            _userName.postValue(name!!)
        }

        override fun onCancelled(error: DatabaseError) {
            _userName.postValue("")
        }

    }

    private val catagoryNameValueListener = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            val name = snapshot.getValue<String>()
            Log.d(TAG, "onDataChange: catagoryName: ${name}")
            _catName.postValue(name!!)
        }

        override fun onCancelled(error: DatabaseError) {
            _catName.postValue("")
        }

    }

    private val brandNameValueListener = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            val name = snapshot.getValue<String>()
            Log.d(TAG, "onDataChange: catagoryName: ${name}")
            _brandName.postValue(name!!)
        }

        override fun onCancelled(error: DatabaseError) {
            _brandName.postValue("")
        }

    }


    private val serviceDataListener = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            val service = snapshot.getValue<Service>()
            _serviceData.postValue(service!!)
        }

        override fun onCancelled(error: DatabaseError) {
        }

    }

    fun getSingleService(catID: String, brandId: String, serviceId: String){
        DbInstance.getDbInstance().reference
            .child(Constants.SERVICE_CATAGORIES)
            .child(catID)
            .child(Constants.brands)
            .child(brandId)
            .child(Constants.SERVICE_LIST)
            .child(serviceId)
            .addListenerForSingleValueEvent(serviceDataListener)
    }

    fun addNewServiceTakenRequest(takenService: ServicesTaken) {
        val rootRef = DbInstance.getDbInstance().reference
            .child(Constants.TakenSericeRoot)
        val key = rootRef.push().key
        takenService.id = key
        rootRef.child(key!!)
            .setValue(takenService)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    _dataAdded.postValue(DbUpdateResult(true, null))
                }else{
                    _dataAdded.postValue(DbUpdateResult(false, null))
                }
            }
    }

    fun getUserNameFromUID(uid: String) {
        DbInstance.getDbInstance().reference.child(Constants.user)
            .child(uid)
            .child("fullName")
            .addValueEventListener(stringValueListener)
    }

    fun getCatagoryName(catID: String) {
        DbInstance.getDbInstance().reference.child(Constants.SERVICE_CATAGORIES)
            .child(catID)
            .child("catagoryName")
            .addValueEventListener(catagoryNameValueListener)
    }

    fun getBrandName(catID: String, brandID: String) {
        DbInstance.getDbInstance().reference.child(Constants.SERVICE_CATAGORIES)
            .child(catID)
            .child(Constants.brands)
            .child(brandID)
            .child("name")
            .addValueEventListener(brandNameValueListener)
    }

}