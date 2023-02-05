package com.example.itservice.admin.show_applications.display_form

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itservice.application.TAG
import com.example.itservice.common.models.AuthResult
import com.example.itservice.common.models.Engineer
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class DisplayFormViewModel: ViewModel() {
    val downloadCompleted = MutableLiveData<AuthResult>()
    private val _isRegistered = MutableLiveData<AuthResult>()
    val isRegistered: LiveData<AuthResult>
    get() = _isRegistered

    private val _formData = MutableLiveData<Engineer?>()
    val formData : LiveData<Engineer?>
    get() = _formData

    private val _formDeleted = MutableLiveData<Boolean>()
    val formDeleted : LiveData<Boolean>
        get() = _formDeleted

    private val _engineerDbUpdated = MutableLiveData<Boolean>()
    val engineerDbUpdated : LiveData<Boolean>
        get() = _engineerDbUpdated

    private val _adminRelogined = MutableLiveData<Boolean>()
    val adminRelogined : LiveData<Boolean>
        get() = _adminRelogined

    private val listener = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            val eng = snapshot.getValue<Engineer>()
            _formData.postValue(eng)
        }

        override fun onCancelled(error: DatabaseError) {
           _formData.postValue(null)
        }
    }

    fun getFormFor(uid: String?) {
        DbInstance.getDbInstance().reference.child(Constants.appliedEngineers)
            .child(uid!!)
            .addListenerForSingleValueEvent(listener)
    }

    fun deleteApplication(uid: String) {
        DbInstance.getDbInstance().reference.child(Constants.appliedEngineers)
            .child(uid)
            .removeValue()
            .addOnCompleteListener {
                if(it.isSuccessful){
                    _formDeleted.postValue(true)
                }else{
                    _formDeleted.postValue(false)
                }
            }
    }

    fun registerEngineer(email: String, password: String) {
        DbInstance.getAuthInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    val newUid = it.result.user?.uid
                    Log.d(TAG, "registerEngineer: new UID "+ newUid)
                    _isRegistered.postValue(AuthResult(true, newUid))
                }else{
                    _isRegistered.postValue(AuthResult(false,it.exception?.message))
                }
            }
    }

    fun registerAdmin(adminEmail: String, adminPassword: String) {
        DbInstance.getAuthInstance().signInWithEmailAndPassword(adminEmail, adminPassword)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    Log.d(TAG, "registerAdmin: success "+ it.result.user?.uid)
                    _adminRelogined.postValue(true)
                }else{
                    Log.d(TAG, "registerAdmin: failed "+ it.exception?.message)
                    _adminRelogined.postValue(false)
                }
            }
    }

    fun updateEngineerTable(engineer: Engineer) {
        val uid = engineer.uid
        Log.d(TAG, "updateEngineerTable: uid "+ uid)
        DbInstance.getDbInstance().reference.child(Constants.engineer)
            .child(engineer.uid!!)
            .setValue(engineer)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    _engineerDbUpdated.postValue(true)
                }else{
                    _engineerDbUpdated.postValue(false)
                }
            }
    }
}