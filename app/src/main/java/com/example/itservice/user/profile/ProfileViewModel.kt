package com.example.itservice.user.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itservice.common.models.User
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class ProfileViewModel : ViewModel() {
    val uploadPhoto = MutableLiveData<String>()

    private var _userInfo = MutableLiveData<User?>()
    val userInfo: LiveData<User?>
    get() = _userInfo

    private var _isProfileUpdated = MutableLiveData<Boolean>()
    val isProfileUpdated: LiveData<Boolean>
        get() = _isProfileUpdated



    private val userInfoListener = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            val user = snapshot.getValue<User>()
            _userInfo.postValue(user)
        }

        override fun onCancelled(error: DatabaseError) {
            _userInfo.postValue(null)
        }

    }
    fun getUserInfo(uid: String){
        DbInstance.getDbInstance().reference.child(Constants.user)
            .child(uid)
            .addValueEventListener(userInfoListener)
    }

    fun updateUserProfilePic(uid: String, path: String?) {
        DbInstance.getDbInstance().reference.child(Constants.user)
            .child(uid)
            .child(Constants.profileImage)
            .setValue(path)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    _isProfileUpdated.postValue(true)
                }else{
                    _isProfileUpdated.postValue(false)
                }
            }
    }
}