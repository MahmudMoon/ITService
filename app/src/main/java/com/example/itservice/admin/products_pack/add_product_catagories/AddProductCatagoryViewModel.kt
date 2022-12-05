package com.example.itservice.admin.products_pack.add_product_catagories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itservice.common.models.DbUpdateResult
import com.example.itservice.common.models.ProductCatagories
import com.example.itservice.common.models.ServiceCatagory
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.google.firebase.database.DatabaseReference

class AddProductCatagoryViewModel: ViewModel() {
    var uploadPhoto: MutableLiveData<String> = MutableLiveData()

    private var _catagoriDataAdded: MutableLiveData<DbUpdateResult> = MutableLiveData()
    val catagoryDataAdded: LiveData<DbUpdateResult>
        get() = _catagoriDataAdded

    fun getRootRef(): DatabaseReference {
        return DbInstance.getDbInstance().reference.child(Constants.ProductCatagories)
    }

    fun getNewKey(): String?{
        val rootRef = getRootRef()
        return rootRef.push().key
    }

    fun storeCatagoryDataInFirebase(productCatagories: ProductCatagories?) {
        val rootRef = getRootRef()
        rootRef.child(productCatagories?.id!!)
            .setValue(productCatagories)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    _catagoriDataAdded.postValue(DbUpdateResult(true, null))
                }else{
                    _catagoriDataAdded.postValue(DbUpdateResult(false, null))
                }
            }
    }
}