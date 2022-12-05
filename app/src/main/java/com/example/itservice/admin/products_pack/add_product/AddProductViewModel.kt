package com.example.itservice.admin.products_pack.add_product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itservice.common.models.DbUpdateResult
import com.example.itservice.common.models.Product
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.google.firebase.database.DatabaseReference

class AddProductViewModel: ViewModel() {
    var uploadPhoto: MutableLiveData<String> = MutableLiveData()

    private var _productDataAdded: MutableLiveData<DbUpdateResult> = MutableLiveData()
    val productDataAdded: LiveData<DbUpdateResult>
        get() = _productDataAdded

    fun getRootRef(): DatabaseReference {
        return DbInstance.getDbInstance().reference.child(Constants.ProductCatagories)
    }

    fun getNewKey(): String?{
        val rootRef = getRootRef()
        return rootRef.push().key
    }

    fun storeserviceDataInFirebase(catId: String ,product: Product?) {
        val rootRef = getRootRef()
            .child(catId)
            .child(Constants.ProductsList)

        rootRef.child(product?.id!!)
            .setValue(product)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    _productDataAdded.postValue(DbUpdateResult(true, null))
                }else{
                    _productDataAdded.postValue(DbUpdateResult(false, null))
                }
            }
    }
}