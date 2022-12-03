package com.example.itservice.admin.service_pack.display_service_catagory.display_service_list.display_service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itservice.common.models.Service
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class DisplayServiceListViewModel: ViewModel() {
    private var _serviceData = MutableLiveData<List<Service>>()
    val serviceData: LiveData<List<Service>>
        get() = _serviceData


    val serviceListListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val serviceList = ArrayList<Service>()
            snapshot.children.forEach { snapshot->
                val service = snapshot.getValue<Service>()
                serviceList.add(service!!)
            }
            _serviceData.postValue(serviceList)
        }

        override fun onCancelled(error: DatabaseError) {

        }

    }

    fun getserviceListForCatagorySelected(catId: String, brandId: String){
        DbInstance.getDbInstance().reference.child(Constants.SERVICE_CATAGORIES)
            .child(catId)
            .child(Constants.brands)
            .child(brandId)
            .child(Constants.SERVICE_LIST)
            .addValueEventListener(serviceListListener)
    }
}