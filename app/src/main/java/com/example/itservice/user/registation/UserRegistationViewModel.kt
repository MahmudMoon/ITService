package com.example.itservice.user.registation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itservice.common.models.AuthResult
import com.example.itservice.common.models.User
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance

class UserRegistationViewModel : ViewModel() {
    var uploadPhoto: MutableLiveData<String> = MutableLiveData()

    private var _userAuthResult = MutableLiveData<AuthResult>()
    val userAuthResult: LiveData<AuthResult>
        get() = _userAuthResult

    fun registerUserWithEmailPassword(fullname: String, email: String, password: String, tin: String, nid: String, companyName: String, companyAddress: String, contactNumber: String ){
        DbInstance.getAuthInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task->
                if(task.isSuccessful){
                    //save data in realtime db
                    val userUid = DbInstance.getAuthInstance().uid
                    val user  = User(uid= userUid,
                        fullName = fullname,
                        email= email,
                        password =  password,
                        tin = tin,
                        nid = nid,
                        companyName =  companyName,
                        companyAddress = companyAddress ,
                        contactNumber = contactNumber )
                    DbInstance.getDbInstance().reference
                        .child(Constants.user)
                        .child(userUid!!)
                        .setValue(user)
                        .addOnCompleteListener{task->
                            if(task.isSuccessful){
                                // stored in db

                                _userAuthResult.postValue(AuthResult(Constants.success,userUid))
                            }else{
                                _userAuthResult.postValue(AuthResult(Constants.failure,null))
                            }
                        }
                }else{
                    //failed to register
                    _userAuthResult.postValue(AuthResult(Constants.failure,null))
                }
            }
    }
}