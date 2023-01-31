package com.example.itservice.admin.offer_modify

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itservice.application.TAG
import com.example.itservice.common.models.Offers
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class OfferModifyViewModel: ViewModel() {
    private var _offerContainer = MutableLiveData<List<Offers>>()
    val offerContainer: LiveData<List<Offers>>
    get() = _offerContainer

    private var _isOfferModified = MutableLiveData<Boolean>()
    val isOfferModified : LiveData<Boolean>
        get() = _isOfferModified

    private var _isOfferDeleted = MutableLiveData<Boolean>()
    val isOfferDeleted : LiveData<Boolean>
        get() = _isOfferDeleted

    val listener = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            val tmpOfferHolder = ArrayList<Offers>()
            snapshot.children.forEach {
                val offer = it.getValue<Offers>()
                tmpOfferHolder.add(offer!!)
            }
            _offerContainer.postValue(tmpOfferHolder)
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d(TAG, "onCancelled: "+error.message)
        }

    }
    fun getAllOffers() {
        DbInstance.getDbInstance().reference.child(Constants.OFFER_LIST)
            .addValueEventListener(listener)
    }

    fun modifyAnOffer(offer: Offers) {
        val root = DbInstance.getDbInstance().reference.child(Constants.OFFER_LIST)
        val key = offer.id
        root.child(key!!).setValue(offer)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    Log.d(TAG, "addAnOffer: success")
                    _isOfferModified.postValue(true)
                }else{
                    Log.d(TAG, "addAnOffer: failure")
                    _isOfferModified.postValue(false)
                }
            }
    }

    fun removeOneOffer(offerID: String) {
        Log.d(TAG, "removeOneOffer: "+offerID)
        val root = DbInstance.getDbInstance().reference.child(Constants.OFFER_LIST)
        root.child(offerID)
            .removeValue()
            .addOnCompleteListener { 
                if(it.isSuccessful){
                    Log.d(TAG, "removeOneOffer: Deleted")
                    _isOfferModified.postValue(true)
                }else{
                    Log.d(TAG, "removeOneOffer: Failed to delete")
                    _isOfferModified.postValue(false)
                }
            }
    }
}