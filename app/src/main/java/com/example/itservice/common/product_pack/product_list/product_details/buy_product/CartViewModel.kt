package com.example.itservice.user.product_catagory.product_list.product_details.buy_product

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itservice.application.TAG
import com.example.itservice.common.models.Product
import com.example.itservice.local_db.DbHelper

class CartViewModel: ViewModel() {
    private var dbHelper: DbHelper? = null
    private var list = MutableLiveData<List<Product>>()
    val cartListLive: LiveData<List<Product>>
    get() = list

    private var selectedList = MutableLiveData<List<Product>>()
    val selectedListLive: LiveData<List<Product>>
        get() = selectedList

    fun setDb(dbHelper: DbHelper){
        this.dbHelper = dbHelper
    }
    fun getAllCarts(){
        val tmpHolder = ArrayList<Product>()
        dbHelper?.getAllCartItems()?.forEach {
           tmpHolder.add(it)
        }
        list.postValue(tmpHolder)
    }

    fun updateDb(productId: String, purchasedQuantity: Int) {
        dbHelper?.updateDb(productId, purchasedQuantity)
    }

    fun deleteFromCartDb(ids: Array<String>){
       val status = dbHelper?.deleteFromTable(ids)
        Log.d(TAG, "deleteFromCartDb: " + status)
        if(status == true){
            getAllCarts()
        }
    }

    fun getSubTotalForProduct(productId: String): Int{
        val product = dbHelper?.checkForExistance(productId)
        val quantity = product?.purchasedProductQuantity
        val price = product?.price
        if(quantity!=null && price != null) {
            val subTotal = quantity * price
            return subTotal
        }
        return 0
    }

    fun updateProductCheckStatus(id: String, status: Boolean) {
        dbHelper?.updateProductStatus(id, status)
    }

    fun deleteSelectedProducts() {
        val tmpHolder = ArrayList<Product>()
        dbHelper?.getSelectedCartItems()?.forEach {
            tmpHolder.add(it)
        }
        selectedList.postValue(tmpHolder)
    }
}