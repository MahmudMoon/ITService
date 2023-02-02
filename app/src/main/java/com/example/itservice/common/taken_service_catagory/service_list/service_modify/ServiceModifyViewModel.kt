package com.example.itservice.common.taken_service_catagory.service_list.service_modify

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itservice.common.models.*
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.Constants.partQuantity
import com.example.itservice.common.utils.DbInstance
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class ServiceModifyViewModel: ViewModel() {
    private var _takenObject = MutableLiveData<ServicesTaken>()
    val takenObject: LiveData<ServicesTaken>
    get() = _takenObject

    private var _dataUpdateCheck = MutableLiveData<DbUpdateResult>()
    val dataUpdateCheck: LiveData<DbUpdateResult>
    get() = _dataUpdateCheck

    private val _engData = MutableLiveData<Engineer>()
    val engData: LiveData<Engineer>
        get() = _engData

    private val listenerForPartList = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
           val list = snapshot.getValue<List<Parts>>()
            val size = list?.size
        }

        override fun onCancelled(error: DatabaseError) {
        }

    }

    private val engineerDataListener = object: ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            val engineer = snapshot.getValue<Engineer>()
            _engData.postValue(engineer!!)
        }

        override fun onCancelled(error: DatabaseError) {
        }

    }

    private val takenServiceListener = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            val servicesTaken = snapshot.getValue<ServicesTaken>()
            _takenObject.postValue(servicesTaken!!)
        }

        override fun onCancelled(error: DatabaseError) {
        }

    }

    fun getTakenServiceObject(createrId: String?, takeServiceId: String?) {
        DbInstance.getDbInstance().reference.child(Constants.TakenSericeRoot)
            .child(takeServiceId!!)
            .addValueEventListener(takenServiceListener)
    }

    fun updateTakenService(takenService: ServicesTaken) {
        DbInstance.getDbInstance().reference.child(Constants.TakenSericeRoot)
            .child(takenService.id!!)
            .setValue(takenService)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    _dataUpdateCheck.postValue(DbUpdateResult(true, null))
                }else{
                    _dataUpdateCheck.postValue(DbUpdateResult(false, null))
                }
            }
    }

    fun getEngineerDataFromUid(engineerUid: String) {
        DbInstance.getDbInstance().reference.child(Constants.engineer)
            .child(engineerUid)
            .addValueEventListener(engineerDataListener)
    }

    fun updateValueOfpartsInRootTable(newlyAddedPart: Parts) {
        newlyAddedPart.partAvailbleAfterRequest = null
        DbInstance.getDbInstance().reference.child(Constants.PartsRoot)
            .child(newlyAddedPart.partID!!)
            .child(Constants.partQuantity)
            .setValue(newlyAddedPart.partQuantity)
    }

    fun updatePartData(discardParts: HashMap<String, Int>) {
        val roofRef = DbInstance.getDbInstance().reference.child(Constants.PartsRoot)
        val iterator =  discardParts.iterator()
        iterator.forEach { data ->
           val res = roofRef.child(data.key).child(partQuantity).addListenerForSingleValueEvent(object : ValueEventListener{
               override fun onDataChange(snapshot: DataSnapshot) {
                   var q = snapshot.getValue<Int>()!!
                   q+=data.value
                   roofRef.child(data.key).child(partQuantity).setValue(q)
               }

               override fun onCancelled(error: DatabaseError) {

               }

           })

        }
        _dataUpdateCheck.postValue(DbUpdateResult(true, null))
    }

    fun sendNotificationFor(assignedEngineerID: String?, createdByID: String?, notifications: Notifications) {
        val notificationRootRef = DbInstance.getDbInstance().reference.child(Constants.Notifications)
        if(assignedEngineerID!=null) sendNotificationForEng(notificationRootRef, assignedEngineerID, notifications)
        if(createdByID != null ) sendNotificationForUser(notificationRootRef, createdByID, notifications)
        sendNotificationForAdmin(notificationRootRef = notificationRootRef, notifications =  notifications)
    }

    private fun sendNotificationForAdmin(notificationRootRef: DatabaseReference, createdByID: String? = null, notifications: Notifications) {
        val userRoot =  notificationRootRef.child(Constants.admin)
        val userNotificationId = userRoot.push().key
        notifications.notificationId = userNotificationId
        userRoot.child(Constants.NotificationFor)
            .child(userNotificationId!!)
            .setValue(notifications)
    }

    private fun sendNotificationForUser(
        notificationRootRef: DatabaseReference,
        createdByID: String?,
        notifications: Notifications
    ) {
        val userRoot =  notificationRootRef.child(Constants.user)
            .child(createdByID!!)
        val userNotificationId = userRoot.push().key
        notifications.notificationId = userNotificationId
        userRoot.child(Constants.NotificationFor)
            .child(userNotificationId!!)
            .setValue(notifications)
    }

    private fun sendNotificationForEng(
        notificationRootRef: DatabaseReference,
        assignedEngineerID: String?,
        notifications: Notifications
    ) {
        val engRoot =  notificationRootRef.child(Constants.engineer)
            .child(assignedEngineerID!!)
        val engNotificationId = engRoot.push().key
        notifications.notificationId = engNotificationId
        engRoot.child(Constants.NotificationFor)
            .child(engNotificationId!!)
            .setValue(notifications)
    }

}