package com.example.itservice.user.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itservice.common.models.AuthResult
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance

class UserLoginViewModel : ViewModel() {
    private var _userAuthResult = MutableLiveData<AuthResult>()
    val userAuthResult: LiveData<AuthResult>
        get() = _userAuthResult

    fun signInUserWithEmailPassword(email: String, password: String){
        DbInstance.getAuthInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task->
                if(task.isSuccessful){
                    //save data in realtime db
                     val uid = task.result.user?.uid
                     //DbInstance.setUserUid(uid)
                    _userAuthResult.postValue(AuthResult(Constants.success,uid))
                }else{
                    //failed to register
                    _userAuthResult.postValue(AuthResult(Constants.failure,null))
                }
            }
    }
}