package com.example.itservice.user.user_dash_board

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.itservice.common.models.Offers
import com.example.itservice.common.utils.DbInstance
import com.example.itservice.local_db.DbHelper

class UserdashboardViewModel: ViewModel() {
    private var offerList = ArrayList<Offers>()
    val offerLiveData: LiveData<List<Offers>>
    get() = offerLiveData

    fun logoutuser() {
        DbInstance.getAuthInstance().signOut()
    }

    fun clearDb(dbHelper: DbHelper) {
        dbHelper.deleteAllEntries()
    }

    fun getAllOffers(){

    }
}