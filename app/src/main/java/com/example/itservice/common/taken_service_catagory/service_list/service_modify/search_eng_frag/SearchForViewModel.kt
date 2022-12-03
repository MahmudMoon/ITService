package com.example.itservice.common.taken_service_catagory.service_list.service_modify.search_eng_frag

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itservice.common.models.Engineer
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class SearchForViewModel: ViewModel() {
    private val _engData = MutableLiveData<List<Engineer>>()
    val engData: LiveData<List<Engineer>>
    get() = _engData

    private val engineerListListener = object: ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            val engList = ArrayList<Engineer>()
            snapshot.children.forEach { snap ->
               val engineer = snap.getValue<Engineer>()
                engList.add(engineer!!)
            }
            _engData.postValue(engList)
        }

        override fun onCancelled(error: DatabaseError) {
        }

    }

    fun getAllEngineers(){
        DbInstance.getDbInstance().reference.child(Constants.engineer)
            .addValueEventListener(engineerListListener)
    }
}