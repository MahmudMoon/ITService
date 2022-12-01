package com.example.itservice.common.splash

import androidx.lifecycle.ViewModel
import com.example.itservice.common.utils.DbInstance
import com.google.firebase.auth.FirebaseUser

class SplashViewModel: ViewModel(){

    fun userLoginData() : FirebaseUser? {
        return DbInstance.getAuthInstance().currentUser
    }
}