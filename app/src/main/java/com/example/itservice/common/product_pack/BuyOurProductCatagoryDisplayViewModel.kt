package com.example.itservice.user.product_catagory

import android.provider.ContactsContract.Data
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itservice.common.models.ProductCatagories
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class BuyOurProductCatagoryDisplayViewModel: ViewModel() {
    private var catList = ArrayList<ProductCatagories>()
    private var _proCatData = MutableLiveData<List<ProductCatagories>>()
    val proCatData: LiveData<List<ProductCatagories>>
    get() = _proCatData

    private var productCatListener = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            catList.clear()
            snapshot.children.forEach { snap->
                val pCat = snap.getValue<ProductCatagories>()
                catList.add(pCat!!)
            }
            _proCatData.postValue(catList)
        }

        override fun onCancelled(error: DatabaseError) {
        }

    }

    fun getProductCatRoot(): DatabaseReference{
        return DbInstance.getDbInstance().reference.child(Constants.ProductCatagories)
    }

    fun getAllProductCatagories(){
        getProductCatRoot().addValueEventListener(productCatListener)
    }
}