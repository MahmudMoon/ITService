package com.example.itservice.enterprise_user.enterprise_user_login.engineer.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itservice.common.models.Admin
import com.example.itservice.common.models.AuthResult
import com.example.itservice.common.models.Engineer
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance

class EngineerRegistrationViewModel: ViewModel() {
    private var _engineerAuthResult = MutableLiveData<AuthResult>()
    val engineerAuthResult: LiveData<AuthResult>
        get() = _engineerAuthResult

    fun registerUserWithEmailPassword(fullName: String,email: String, password: String, companyName: String, employeeID: String, nid: String){
        DbInstance.getAuthInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task->
                if(task.isSuccessful){
                    //save data in realtime db
                    val engineerUid = DbInstance.getAuthInstance().uid
                    val engineer  = Engineer(uid= engineerUid, email= email, password =  password,
                        fullName = fullName, companyName =  companyName, employeeID =  employeeID, NID =  nid )
                    DbInstance.getDbInstance().reference
                        .child(Constants.engineer)
                        .child(engineerUid!!)
                        .setValue(engineer)
                        .addOnCompleteListener{task->
                            if(task.isSuccessful){
                                // stored in db
                                _engineerAuthResult.postValue(AuthResult(Constants.success,null))
                            }else{
                                _engineerAuthResult.postValue(AuthResult(Constants.failure,null))
                            }
                        }
                }else{
                    //failed to register
                    _engineerAuthResult.postValue(AuthResult(Constants.failure,null))
                }
            }
    }
}