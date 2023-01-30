package com.example.itservice.user.user_dash_board

import androidx.lifecycle.ViewModel
import com.example.itservice.common.utils.DbInstance
import com.example.itservice.local_db.DbHelper

class UserdashboardViewModel: ViewModel() {
    fun logoutuser() {
        DbInstance.getAuthInstance().signOut()
    }

    fun clearDb(dbHelper: DbHelper) {
        dbHelper.deleteAllEntries()
    }
}