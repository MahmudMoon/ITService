package com.example.itservice.admin.show_applications

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itservice.application.TAG
import com.example.itservice.common.models.Engineer
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class DisplayApplicationListViewModel: ViewModel() {
    private var _engList = MutableLiveData<List<Engineer>?>()
    val engList: LiveData<List<Engineer>?>
    get() = _engList

    private var listOfApplications = ArrayList<Engineer>()
    private val engListInfoListener = object
        : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            listOfApplications.clear()
            snapshot.children.forEach {
                val tmp = it.getValue<Engineer>()
                if(tmp!=null) {
                    Log.d(TAG, "onDataChange: fetching eng list "+tmp.email)
                    listOfApplications.add(tmp)
                }
            }
            _engList.postValue(listOfApplications)
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d(TAG, "onCancelled: fetching eng list "+error.message)
            _engList.postValue(null)
        }

    }
    fun getAllApplications() {
        DbInstance.getDbInstance().reference.child(Constants.appliedEngineers)
            .addValueEventListener(engListInfoListener)
    }
}