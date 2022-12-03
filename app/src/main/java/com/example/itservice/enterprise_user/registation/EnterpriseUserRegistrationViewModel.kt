package com.example.itservice.enterprise_user.registation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itservice.common.models.AuthResult
import com.example.itservice.common.models.Enterprise_user
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance

class EnterpriseUserRegistrationViewModel: ViewModel() {
    private var _enterprise_userAuthResult = MutableLiveData<AuthResult>()
    val enterprise_userAuthResult: LiveData<AuthResult>
        get() = _enterprise_userAuthResult

    fun registerUserWithEmailPassword(email: String, password: String, tin: String, companyName: String, companyAddress: String, contactNumber: String ){
        DbInstance.getAuthInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task->
                if(task.isSuccessful){
                    //save data in realtime db
                    val enterprise_userUid = DbInstance.getAuthInstance().uid
                    val enterprise_user  = Enterprise_user(uid= enterprise_userUid,
                        email= email,
                        password =  password,
                        tin = tin,
                        companyName =  companyName,
                        companyAddress = companyAddress ,
                        contactNumber = contactNumber )
                    DbInstance.getDbInstance().reference
                        .child(Constants.user)
                        .child(enterprise_userUid!!)
                        .setValue(enterprise_user)
                        .addOnCompleteListener{task->
                            if(task.isSuccessful){
                                // stored in db
                                _enterprise_userAuthResult.postValue(AuthResult(Constants.success,enterprise_userUid))
                            }else{
                                _enterprise_userAuthResult.postValue(AuthResult(Constants.failure,null))
                            }
                        }
                }else{
                    //failed to register
                    _enterprise_userAuthResult.postValue(AuthResult(Constants.failure,null))
                }
            }
    }
}