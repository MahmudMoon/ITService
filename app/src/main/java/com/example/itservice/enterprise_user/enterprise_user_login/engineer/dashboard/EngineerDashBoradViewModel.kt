package com.example.itservice.enterprise_user.enterprise_user_login.engineer.dashboard

import androidx.lifecycle.ViewModel
import com.example.itservice.common.utils.DbInstance

class EngineerDashBoradViewModel: ViewModel() {
    fun logoutuser() {
        DbInstance.getAuthInstance().signOut()
    }
}