package com.example.itservice.admin.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itservice.common.models.Admin
import com.example.itservice.common.models.AuthResult
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class AdminLoginViewModel: ViewModel() {
    private var _adminAuthResult = MutableLiveData<AuthResult>()
    val adminAuthResult: LiveData<AuthResult>
        get() = _adminAuthResult

    private val _isAdminExist = MutableLiveData<Boolean>()
    val isAdminExist : LiveData<Boolean>
    get() = _isAdminExist

    private val listener = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            val admin = snapshot.getValue<Admin>()
            if(admin!=null) {
                _isAdminExist.postValue(true)
            }else{
                _isAdminExist.postValue(false)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            _isAdminExist.postValue(false)
        }

    }

    fun signInUserWithEmailPassword(email: String, password: String){
        DbInstance.getAuthInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task->
                if(task.isSuccessful){
                    val uid = task.result.user?.uid
                    //save data in realtime db
                    _adminAuthResult.postValue(AuthResult(Constants.success,uid))
                }else{
                    //failed to register
                    _adminAuthResult.postValue(AuthResult(Constants.failure,null))
                }
            }
    }

    fun isAdminExists(uid: String) {
        DbInstance.getDbInstance().reference.child(Constants.admin)
            .child(uid)
            .addValueEventListener(listener)
    }
}
