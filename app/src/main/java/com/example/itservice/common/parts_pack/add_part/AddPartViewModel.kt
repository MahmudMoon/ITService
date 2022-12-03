package com.example.itservice.common.parts_pack.add_part

import android.provider.Telephony.Mms.Part
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itservice.common.models.DbUpdateResult
import com.example.itservice.common.models.Parts
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class AddPartViewModel : ViewModel() {
    private var _partAdded = MutableLiveData<DbUpdateResult>()
    val partAdded: LiveData<DbUpdateResult>
    get() = _partAdded

    private var _partData = MutableLiveData<Parts>()
    val partData: LiveData<Parts>
        get() = _partData

    private val partDataListener = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            val part = snapshot.getValue<Parts>()
            _partData.postValue(part!!)
        }

        override fun onCancelled(error: DatabaseError) {
        }

    }

    private fun getpartRoot(): DatabaseReference {
        return DbInstance.getDbInstance().reference
            .child(Constants.PartsRoot)
    }

    fun addNewPart(part: Parts) {
        val root = getpartRoot()
        val partUid = if (part.partID != null) part.partID else root.push().key
        part.partID = partUid
        root.child(partUid!!).setValue(part)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    _partAdded.postValue(DbUpdateResult(true, null))
                }else{
                    _partAdded.postValue(DbUpdateResult(false, null))
                }
            }
    }

    fun getPartForId(partID: String){
        getpartRoot().child(partID).addValueEventListener(partDataListener)
    }

}