package com.example.itservice.admin.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itservice.common.models.Admin
import com.example.itservice.common.models.AuthResult
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance

class AdminRegistrationViewModel: ViewModel() {

    private var _adminAuthResult = MutableLiveData<AuthResult>()
    val adminAuthResult: LiveData<AuthResult>
    get() = _adminAuthResult

    fun registerUserWithEmailPassword(fullName: String,email: String, password: String){
        DbInstance.getAuthInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task->
                if(task.isSuccessful){
                    //save data in realtime db
                    val adminUid = DbInstance.getAuthInstance().uid
                    val admin  = Admin(adminUid, fullName, email, password )
                    val dbRef = DbInstance.getDbInstance().reference
                        .child(Constants.admin)
                        .child(adminUid!!)
                        .setValue(admin)
                        .addOnCompleteListener{task->
                            if(task.isSuccessful){
                                // stored in db
                                _adminAuthResult.postValue(AuthResult(Constants.success,adminUid))
                            }else{
                                _adminAuthResult.postValue(AuthResult(Constants.failure,null))
                            }
                        }
                }else{
                    //failed to register
                    _adminAuthResult.postValue(AuthResult(Constants.failure,null))
                }
            }
    }
}