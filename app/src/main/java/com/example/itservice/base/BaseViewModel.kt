package com.example.itservice.base

import androidx.lifecycle.ViewModel
import com.example.itservice.common.utils.DbInstance
import com.example.itservice.local_db.DbHelper

class BaseViewModel: ViewModel() {
    fun logoutuser() {
        DbInstance.getAuthInstance().signOut()
    }
    fun clearDb(dbHelper: DbHelper) {
        dbHelper.deleteAllEntries()
    }
}