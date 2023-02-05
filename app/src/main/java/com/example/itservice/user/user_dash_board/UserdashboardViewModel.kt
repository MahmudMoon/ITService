package com.example.itservice.user.user_dash_board

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itservice.application.TAG
import com.example.itservice.common.models.Offers
import com.example.itservice.common.models.User
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.example.itservice.local_db.DbHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import java.util.Objects

class UserdashboardViewModel: ViewModel() {
    private var offerList = MutableLiveData<List<Offers>>()
    val offerLiveData: LiveData<List<Offers>>
        get() = offerList

    private var _userInfo = MutableLiveData<User?>()
    val userInfo: LiveData<User?>
        get() = _userInfo

    private val userInfoListener = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            val user = snapshot.getValue<User>()
            _userInfo.postValue(user)
        }

        override fun onCancelled(error: DatabaseError) {
            _userInfo.postValue(null)
        }

    }



    val listener = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            val offerTmpHolder = ArrayList<Offers>()
            snapshot.children.forEach {
                val offer = it.getValue<Offers>()
                offerTmpHolder.add(offer!!)
            }
            offerList.postValue(offerTmpHolder)
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d(TAG, "onCancelled: "+error.message)
        }

    }

    fun logoutuser() {
        DbInstance.getAuthInstance().signOut()
    }

    fun clearDb(dbHelper: DbHelper) {
        dbHelper.deleteAllEntries()
    }

    fun getAllOffers(){
        DbInstance.getDbInstance().reference.child(Constants.OFFER_LIST)
            .addValueEventListener(listener)
    }

    fun getUserInfo(uid: String) {
        DbInstance.getDbInstance().reference.child(Constants.user)
            .child(uid)
            .addValueEventListener(userInfoListener)
    }
}
