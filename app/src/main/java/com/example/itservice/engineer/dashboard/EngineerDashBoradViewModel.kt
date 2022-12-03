package com.example.itservice.engineer.dashboard

import androidx.lifecycle.ViewModel
import com.example.itservice.common.utils.DbInstance

class EngineerDashBoradViewModel: ViewModel() {
    fun logoutuser() {
        DbInstance.getAuthInstance().signOut()
    }
}