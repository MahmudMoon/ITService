package com.example.itservice.engineer.login_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itservice.common.models.AuthResult
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance

class EngineerLoginViewModel : ViewModel() {
    private var _engineerAuthResult = MutableLiveData<AuthResult>()
    val engineerAuthResult: LiveData<AuthResult>
        get() = _engineerAuthResult

    fun signInUserWithEmailPassword(email: String, password: String){
        DbInstance.getAuthInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task->
                if(task.isSuccessful){
                    val uid = task.result.user?.uid
                    //save data in realtime db
                    _engineerAuthResult.postValue(AuthResult(Constants.success,uid))
                }else{
                    //failed to register
                    _engineerAuthResult.postValue(AuthResult(Constants.failure,null))
                }
            }
    }
}