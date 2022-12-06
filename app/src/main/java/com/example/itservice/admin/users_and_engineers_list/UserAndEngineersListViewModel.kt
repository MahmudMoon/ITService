package com.example.itservice.admin.users_and_engineers_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itservice.common.models.Engineer
import com.example.itservice.common.models.User
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class UserAndEngineersListViewModel: ViewModel() {
    private val _engsData = MutableLiveData<List<Engineer>>()
    val engsData: LiveData<List<Engineer>>
    get() = _engsData

    private val _usersData = MutableLiveData<List<User>>()
    val usersData: LiveData<List<User>>
        get() = _usersData

    private val engineerValueListener = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            val tmpList = ArrayList<Engineer>()
            snapshot.children.forEach { value ->
                val engineer = value.getValue<Engineer>()
                tmpList.add(engineer!!)
            }
            _engsData.postValue(tmpList)
        }

        override fun onCancelled(error: DatabaseError) {
        }

    }

    fun getEngineersList(){
        DbInstance.getDbInstance().reference
            .child(Constants.engineer)
            .addValueEventListener(engineerValueListener)
    }



    private val usersValueListener = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            val tmpList = ArrayList<User>()
            snapshot.children.forEach { value ->
                val user = value.getValue<User>()
                tmpList.add(user!!)
            }
            _usersData.postValue(tmpList)
        }

        override fun onCancelled(error: DatabaseError) {
        }

    }
    fun getUsersList(){
        DbInstance.getDbInstance().reference
            .child(Constants.user)
            .addValueEventListener(usersValueListener)
    }
}