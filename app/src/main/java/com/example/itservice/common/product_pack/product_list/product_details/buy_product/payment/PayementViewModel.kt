package com.example.itservice.common.product_pack.product_list.product_details.buy_product.payment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itservice.application.TAG
import com.example.itservice.common.models.Product
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.example.itservice.local_db.DbHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class PayementViewModel: ViewModel() {
    val root =  DbInstance.getDbInstance().reference.child(Constants.ProductCatagories)

    private var _purchasableProducts = MutableLiveData<List<Product>>()
    val purchaseableProducts: LiveData<List<Product>>
    get() = _purchasableProducts

    var dbHelper: DbHelper? = null

    private var _seletedCarts = MutableLiveData<List<Product>>()
    val selectedCarts: LiveData<List<Product>>
        get() = _seletedCarts


    private var _productTableUpdated = MutableLiveData<Boolean>()
    val productTableUpdated: LiveData<Boolean>
        get() = _productTableUpdated


    private var _deletedFromLocal = MutableLiveData<List<Boolean>>()
    val deletedFromLocal: LiveData<List<Boolean>>
        get() = _deletedFromLocal


    fun confirmPayment(dbHelper: DbHelper) {
        //delete seleted card from local db
        //reduce the product quantity
        // display a dialog of payment confirmation
        this.dbHelper = dbHelper
        val selectedCarts = dbHelper.getSelectedCartItems()
        val maxCount = selectedCarts.size
        val tmpHolder = ArrayList<Product>()
        selectedCarts.forEach {
            Log.d(TAG, "confirmPayment: ${it.catID} , ${it.id} , ${it.purchasedProductQuantity}")
            root.child(it.catID!!)
                .child(Constants.ProductsList)
                .child(it.id!!)
                .addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var product = snapshot.getValue<Product>()
                        var availableQuantity = product?.quantity
                        Log.d(TAG, "purchaseableProducts: before ${it.id} , ${it.quantity}")
                        if(availableQuantity!=null){
                            if(availableQuantity >= it.purchasedProductQuantity!!){
                                availableQuantity = availableQuantity - it.purchasedProductQuantity!!
                                product?.quantity = availableQuantity
                                tmpHolder.add(product!!)
                                Log.d(TAG, "purchaseableProducts: after ${it.id} , ${it.quantity}")
                                if(tmpHolder.size == maxCount){
                                    _purchasableProducts.postValue(tmpHolder)
                                }
                            }else{
                                // purchase not available due to shortage of products

                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e(TAG, "onCancelled: ${error.message}" )
                    }

                })
        }
    }

    fun updateProductQuantityInFirebase(updatedQuantity: List<Product>?) {
        val maxCount = updatedQuantity?.size
        var count = 0
        updatedQuantity?.forEach {
            root.child(it.catID!!)
                .child(Constants.ProductsList)
                .child(it.id!!)
                .child(Constants.ProductQuantity)
                .setValue(it.quantity)
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        count+=1
                        if(maxCount == count){
                            _productTableUpdated.postValue(true)
                        }
                    }else{
                        count+=1
                        if(maxCount == count){
                            _productTableUpdated.postValue(false)
                        }
                    }
                }
        }
    }

    fun getSeletedCarts() {
        val tmpHolder = ArrayList<Product>()
        if(dbHelper!=null){
            dbHelper?.getSelectedCartItems()?.forEach {
                tmpHolder.add(it)
            }
        }
        _seletedCarts.postValue(tmpHolder)
    }

    fun deleteSeletedCarts(ids: Array<String>) {
       var status = ArrayList<Boolean>()
        if(dbHelper!=null){
           status.add(dbHelper?.deleteFromTable(ids)!!)
        }
        _deletedFromLocal.postValue(status)
    }
}