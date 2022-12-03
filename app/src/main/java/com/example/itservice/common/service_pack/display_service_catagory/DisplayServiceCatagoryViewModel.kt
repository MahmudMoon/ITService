package com.example.itservice.common.service_pack.display_service_catagory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itservice.application.TAG
import com.example.itservice.common.models.ServiceCatagory
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class DisplayServiceCatagoryViewModel: ViewModel() {
    private var _catagoryList = MutableLiveData<List<ServiceCatagory>>()
    val catagoryList: LiveData<List<ServiceCatagory>>
    get() = _catagoryList

    val catagoryEventListen = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<ServiceCatagory>()
                snapshot.children.forEach { snapshot ->
                    val serviceCatagory = snapshot.getValue<ServiceCatagory>()
                    list.add(serviceCatagory!!)
                }
                _catagoryList.postValue(list)
        }

        override fun onCancelled(error: DatabaseError) {
        }

    }


    fun getServiceCatagories(){
        DbInstance.getDbInstance().reference.child(Constants.SERVICE_CATAGORIES)
            .addValueEventListener(catagoryEventListen)
    }
}