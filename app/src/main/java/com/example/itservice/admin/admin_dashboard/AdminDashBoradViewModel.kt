package com.example.itservice.admin.admin_dashboard

import androidx.lifecycle.ViewModel
import com.example.itservice.common.utils.DbInstance

class AdminDashBoradViewModel: ViewModel() {
    fun logoutuser() {
        DbInstance.getAuthInstance().signOut()
    }


}