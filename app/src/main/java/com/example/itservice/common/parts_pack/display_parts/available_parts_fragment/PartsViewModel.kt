package com.example.itservice.common.parts_pack.display_parts.available_parts_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itservice.common.models.Parts
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class PartsViewModel: ViewModel() {
    private val _partData = MutableLiveData<List<Parts>>()
    val partData: LiveData<List<Parts>>
        get() = _partData


    private val partsValueListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val partAry = ArrayList<Parts>()
            snapshot.children.forEach {
                val part = it.getValue<Parts>()
                partAry.add(part!!)
            }
            _partData.postValue(partAry)
        }

        override fun onCancelled(error: DatabaseError) {
        }

    }

    fun getAllParts(){
        DbInstance.getDbInstance().reference.child(Constants.PartsRoot)
            .addValueEventListener(partsValueListener)
    }
}