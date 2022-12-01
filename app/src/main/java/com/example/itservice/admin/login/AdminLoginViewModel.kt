package com.example.itservice.admin.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itservice.common.models.Admin
import com.example.itservice.common.models.AuthResult
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance

class AdminLoginViewModel: ViewModel() {
    private var _adminAuthResult = MutableLiveData<AuthResult>()
    val adminAuthResult: LiveData<AuthResult>
        get() = _adminAuthResult

    fun signInUserWithEmailPassword(email: String, password: String){
        DbInstance.getAuthInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task->
                if(task.isSuccessful){
                    //save data in realtime db
                    _adminAuthResult.postValue(AuthResult(Constants.success,null))
                }else{
                    //failed to register
                    _adminAuthResult.postValue(AuthResult(Constants.failure,null))
                }
            }
    }
}
