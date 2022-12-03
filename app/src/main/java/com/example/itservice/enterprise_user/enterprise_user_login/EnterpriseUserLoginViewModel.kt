package com.example.itservice.enterprise_user.enterprise_user_login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itservice.common.models.AuthResult
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance

class EnterpriseUserLoginViewModel : ViewModel() {
    private var _enterpriseuserAuthResult = MutableLiveData<AuthResult>()
    val enterpriseuserAuthResult: LiveData<AuthResult>
        get() = _enterpriseuserAuthResult

    fun signInUserWithEmailPassword(email: String, password: String){
        DbInstance.getAuthInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task->
                if(task.isSuccessful){
                    val uid = task.result.user?.uid
                    //save data in realtime db
                    _enterpriseuserAuthResult.postValue(AuthResult(Constants.success,uid))
                }else{
                    //failed to register
                    _enterpriseuserAuthResult.postValue(AuthResult(Constants.failure,null))
                }
            }
    }
}