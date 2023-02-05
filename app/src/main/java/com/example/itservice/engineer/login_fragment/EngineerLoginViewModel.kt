package com.example.itservice.engineer.login_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itservice.common.models.AuthResult
import com.example.itservice.common.models.Engineer
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class EngineerLoginViewModel : ViewModel() {
    private var _engineerAuthResult = MutableLiveData<AuthResult>()
    val engineerAuthResult: LiveData<AuthResult>
        get() = _engineerAuthResult

    private var _isEngineerExists = MutableLiveData<Boolean>()
    val isEngineerExists: LiveData<Boolean>
        get() = _isEngineerExists

    private val listener = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            val eng = snapshot.getValue<Engineer>()
            if(eng!=null){
                _isEngineerExists.postValue(true)
            }else{
                _isEngineerExists.postValue(false)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            _isEngineerExists.postValue(false)
        }

    }
    
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

    fun isEngineerExist(uid: String) {
        DbInstance.getDbInstance().reference.child(Constants.engineer)
            .child(uid)
            .addValueEventListener(listener)
    }
}