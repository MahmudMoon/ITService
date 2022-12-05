package com.example.itservice.admin.admin_dashboard

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.itservice.application.TAG
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class AdminDashBoradViewModel: ViewModel() {
    fun logoutuser() {
        DbInstance.getAuthInstance().signOut()
    }
}