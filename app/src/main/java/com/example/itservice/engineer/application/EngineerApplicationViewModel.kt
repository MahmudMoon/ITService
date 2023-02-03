package com.example.itservice.engineer.application

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itservice.common.models.AuthResult
import com.example.itservice.common.models.Engineer
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance

class EngineerApplicationViewModel : ViewModel() {
    var uploadCV: MutableLiveData<String> = MutableLiveData()

    private var _engineerApplyDataSave = MutableLiveData<AuthResult>()
    val engineerApplyDataSave: LiveData<AuthResult>
        get() = _engineerApplyDataSave

    private var _isCVUpdated = MutableLiveData<Boolean>()
    val isCVUpdated: LiveData<Boolean>
        get() = _isCVUpdated

    fun applyEngineerWithEmailPassword(
        fullName: String,
        email: String,
        password: String,
        contactNumber: String,
        companyName: String,
        employeeID: String,
        nid: String,
        engineerCatagory: String
    ) {

        val root = DbInstance.getDbInstance().reference
            .child(Constants.appliedEngineers)
        val key = root.push().key
        val engineerUid = key
        val engineer = Engineer(
            uid = engineerUid,
            email = email,
            password = password,
            fullName = fullName,
            companyName = companyName,
            employeeID = employeeID,
            NID = nid,
            contactNumber = contactNumber,
            catagory = engineerCatagory
        )

        root.child(key!!)
            .setValue(engineer)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // stored in db
                    _engineerApplyDataSave.postValue(AuthResult(Constants.success, engineerUid))
                } else {
                    _engineerApplyDataSave.postValue(AuthResult(Constants.failure, null))
                }
            }


    }

    fun updateCvLink(userUid: String ,path: String?) {
        DbInstance.getDbInstance().reference.child(Constants.appliedEngineers)
            .child(userUid)
            .child(Constants.cvLink)
            .setValue(path)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    _isCVUpdated.postValue(true)
                }else{
                    _isCVUpdated.postValue(false)
                }
            }
    }
}