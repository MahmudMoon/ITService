package com.example.itservice.user.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itservice.application.TAG
import com.example.itservice.common.models.AuthResult
import com.example.itservice.common.models.User
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class UserLoginViewModel : ViewModel() {
    private var _userAuthResult = MutableLiveData<AuthResult>()
    val userAuthResult: LiveData<AuthResult>
        get() = _userAuthResult

    private var _userData = MutableLiveData<User>()
    val userData: LiveData<User>
        get() = _userData


    val listener = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            val user = snapshot.getValue<User>()
            _userData.postValue(user!!)
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d(TAG, "onCancelled: error on fatching user")
        }

    }

    fun getUserData(uid: String){
        DbInstance.getDbInstance().reference.child(Constants.user)
            .child(uid)
            .addListenerForSingleValueEvent(listener)
    }


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