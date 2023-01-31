package com.example.itservice.user.product_catagory.product_list.product_details.buy_product

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

class CartViewModel: ViewModel() {
    private var dbHelper: DbHelper? = null

    private var list = MutableLiveData<List<Product>>()
    val cartListLive: LiveData<List<Product>>
    get() = list

    private var _priceUpdated = MutableLiveData<List<Product>>()
    val priceUpdated: LiveData<List<Product>>
        get() = _priceUpdated

    var count = 0
    var maxCount = 0

    private var selectedList = MutableLiveData<List<Product>>()
    val selectedListLive: LiveData<List<Product>>
        get() = selectedList

    val listener = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            snapshot.children.forEach {
                it.ref.child(Constants.ProductsList)
                    .addValueEventListener(productListener)
            }
        }

        override fun onCancelled(error: DatabaseError) {

        }

    }

    val productListener = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
           val product = snapshot.getValue<Product>()
            val id = product?.id
            Log.d(TAG, "onDataChange: inside cart "+id)
        }

        override fun onCancelled(error: DatabaseError) {

        }

    }

    fun setDb(dbHelper: DbHelper){
        this.dbHelper = dbHelper
    }
    fun getAllCarts(state: Boolean? = null){
        val tmpHolder = ArrayList<Product>()
        dbHelper?.getAllCartItems()?.forEach {
           tmpHolder.add(it)
        }
        maxCount = tmpHolder.size
        if(state==null)
            list.postValue(tmpHolder)
        else{
            _priceUpdated.postValue(tmpHolder)
        }
    }

    fun updateDb(productId: String, purchasedQuantity: Int) {
        dbHelper?.updateDb(productId, purchasedQuantity)
    }

    fun updatePriceColumnDb(productId: String, price: Int) {
        dbHelper?.updatePriceColumnInDb(productId, price)
        if(count == maxCount){
            getAllCarts(true)
        }
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

    fun checkForPriceUpdate(products: List<Product>) {
        products.forEach {
            val catID = it.catID
            val productId = it.id
            DbInstance.getDbInstance().reference.child(Constants.ProductCatagories)
                .child(catID!!)
                .child(Constants.ProductsList)
                .child(productId!!)
                .addValueEventListener(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                           val product = snapshot.getValue<Product>()
                            if(product?.offeredPrice!=null){
                                count+=1
                                updatePriceColumnDb(productId, product.offeredPrice!!)
                            }else if(product?.price != null) {
                                count+=1
                                updatePriceColumnDb(productId, product.price!!)
                            }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })
        }

    }
}