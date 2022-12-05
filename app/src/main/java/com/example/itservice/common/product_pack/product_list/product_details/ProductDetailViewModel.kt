package com.example.itservice.user.product_catagory.product_list.product_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itservice.common.models.Product
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class ProductDetailViewModel: ViewModel() {
    private var _productData = MutableLiveData<Product>()
    val productData: LiveData<Product>
        get() = _productData

    private val productValueListener = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
           val product = snapshot.getValue<Product>()
            _productData.postValue(product!!)
        }

        override fun onCancelled(error: DatabaseError) {
        }

    }
    fun getSignleProduct(catId: String, productId: String){
        DbInstance.getDbInstance().reference
            .child(Constants.ProductCatagories)
            .child(catId)
            .child(Constants.ProductsList)
            .child(productId)
            .addValueEventListener(productValueListener)
    }
}