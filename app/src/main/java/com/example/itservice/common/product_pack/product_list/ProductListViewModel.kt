package com.example.itservice.user.product_catagory.product_list

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

class ProductListViewModel: ViewModel() {
    private var _productsData = MutableLiveData<List<Product>>()
    val productsData: LiveData<List<Product>>
        get() = _productsData


    val productsListListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val productsList = ArrayList<Product>()
            snapshot.children.forEach { snap->
                val product = snap.getValue<Product>()
                productsList.add(product!!)
            }
            _productsData.postValue(productsList)
        }

        override fun onCancelled(error: DatabaseError) {

        }

    }

    fun getserviceListForCatagorySelected(catId: String){
        DbInstance.getDbInstance().reference.child(Constants.ProductCatagories)
            .child(catId)
            .child(Constants.ProductsList)
            .addValueEventListener(productsListListener)
    }
}