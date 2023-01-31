package com.example.itservice.admin.addOffers

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itservice.application.TAG
import com.example.itservice.common.models.Offers
import com.example.itservice.common.models.Product
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class AddOfferViewModel: ViewModel() {
    private var _productsData = MutableLiveData<List<Product>>()
    val productsData: LiveData<List<Product>>
        get() = _productsData
    val productsList = ArrayList<Product>()

    private var _isOfferAdded = MutableLiveData<Boolean>()
    val isOfferAdded : LiveData<Boolean>
    get() = _isOfferAdded


    val catagoryReader = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            productsList.clear()
            snapshot.children.forEach {
                it.ref.child(Constants.ProductsList)
                    .addListenerForSingleValueEvent(productsListListener)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d(TAG, "onCancelled: "+error.message)
        }

    }


    val productsListListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            snapshot.children.forEach { snap->
                val product = snap.getValue<Product>()
                Log.d(TAG, "onDataChange: "+product?.id)
                productsList.add(product!!)
            }
            _productsData.postValue(productsList)
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d(TAG, "onCancelled: "+error.message)
        }

    }

    fun getserviceListForCatagorySelected(){
        DbInstance.getDbInstance().reference.child(Constants.ProductCatagories)
            .addListenerForSingleValueEvent(catagoryReader)
    }

    fun addAnOffer(offer: Offers){
        val root = DbInstance.getDbInstance().reference.child(Constants.OFFER_LIST)
        val key = offer.productID
        offer.id = key
        root.child(key!!).setValue(offer)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    Log.d(TAG, "addAnOffer: success")
                    _isOfferAdded.postValue(true)
                }else{
                    Log.d(TAG, "addAnOffer: failure")
                    _isOfferAdded.postValue(false)
                }
            }
    }

    fun addOfferPriceInProductTable(productId: String?, catID: String?, price: Int) {
        DbInstance.getDbInstance().reference.child(Constants.ProductCatagories)
            .child(catID!!)
            .child(Constants.ProductsList)
            .child(productId!!)
            .child(Constants.ProductOfferPrice)
            .setValue(price)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    Log.d(TAG, "changeProductPrice: success")
                }else{
                    Log.d(TAG, "changeProductPrice: failed")
                }
            }
    }
}
