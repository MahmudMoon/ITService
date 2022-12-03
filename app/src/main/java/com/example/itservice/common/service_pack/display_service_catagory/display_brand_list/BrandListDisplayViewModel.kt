package com.example.itservice.common.service_pack.display_service_catagory.display_brand_list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itservice.application.TAG
import com.example.itservice.common.models.Brand
import com.example.itservice.common.models.ServiceCatagory
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import kotlin.math.log

class BrandListDisplayViewModel : ViewModel() {
    private var _brandData = MutableLiveData<List<Brand>>()
    val brandData: LiveData<List<Brand>>
        get() = _brandData


    val brandListListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            try {
                val brandList = ArrayList<Brand>()
                snapshot.children.forEach { snapshot ->
                    val brand = snapshot.getValue<Brand>()
                    brandList.add(brand!!)
                }
                _brandData.postValue(brandList)
            } catch (e: java.lang.Exception) {
                Log.e(TAG, "onDataChange: ${e.localizedMessage}")
            }
        }

        override fun onCancelled(error: DatabaseError) {

        }


    }

    fun getBrandListForCatagorySelected(catId: String) {
        DbInstance.getDbInstance().reference.child(Constants.SERVICE_CATAGORIES)
            .child(catId)
            .child(Constants.brands)
            .addValueEventListener(brandListListener)
    }
}