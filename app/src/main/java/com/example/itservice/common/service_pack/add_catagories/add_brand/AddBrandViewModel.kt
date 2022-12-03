package com.example.itservice.admin.service_pack.add_service.add_brandes.add_brand

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itservice.common.models.Brand
import com.example.itservice.common.models.DbUpdateResult
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.google.firebase.database.DatabaseReference

class AddBrandViewModel: ViewModel() {
    var uploadPhoto: MutableLiveData<String> = MutableLiveData()

    private var _brandDataAdded: MutableLiveData<DbUpdateResult> = MutableLiveData()
    val brandDataAdded: LiveData<DbUpdateResult>
        get() = _brandDataAdded

    fun getRootRef(): DatabaseReference {
        return DbInstance.getDbInstance().reference.child(Constants.SERVICE_CATAGORIES)
    }

    fun getNewKey(): String?{
        val rootRef = getRootRef()
        return rootRef.push().key
    }

    fun storebrandDataInFirebase(catId: String ,brand: Brand?) {
        val rootRef = getRootRef().child(catId).child(Constants.brands)
        rootRef.child(brand?.id!!)
            .setValue(brand)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    _brandDataAdded.postValue(DbUpdateResult(true, null))
                }else{
                    _brandDataAdded.postValue(DbUpdateResult(false, null))
                }
            }
    }
}