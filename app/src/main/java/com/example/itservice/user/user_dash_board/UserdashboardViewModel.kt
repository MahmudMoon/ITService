package com.example.itservice.user.user_dash_board

import androidx.lifecycle.ViewModel
import com.example.itservice.common.utils.DbInstance

class UserdashboardViewModel: ViewModel() {
    fun logoutuser() {
        DbInstance.getAuthInstance().signOut()
    }
}