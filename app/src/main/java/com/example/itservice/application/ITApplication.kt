package com.example.itservice.application

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.itservice.R
import com.example.itservice.common.models.Notifications
import com.example.itservice.common.taken_service_catagory.service_list.service_modify.ServiceModifyActivity
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.Constants.CHANNEL_ID
import com.example.itservice.common.utils.DbInstance
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.getValue
import java.util.*

public const val TAG = "ITApplication"

class ITApplication: Application() {


    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: ")
        createNotificationChannel()
        if(DbInstance.getLastLoginAs(applicationContext).equals(Constants.admin))  listenforAdminNotifications()
        if(DbInstance.getLastLoginAs(applicationContext).equals(Constants.user))  listenforUserNotifications()
        if(DbInstance.getLastLoginAs(applicationContext).equals(Constants.engineer))  listenforEngineerNotifications()
    }

    fun openNotification(takenServiceId: String?, createdById: String?) {
        val serviceIntent = Intent(this, ServiceModifyActivity::class.java)
        serviceIntent.putExtra(Constants.takenServiceId, takenServiceId)
        serviceIntent.putExtra(Constants.userID, createdById)

        val servicePendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            // Add the intent, which inflates the back stack
            addNextIntentWithParentStack(serviceIntent)
            // Get the PendingIntent containing the entire back stack
            getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }

        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.admin)
            .setContentTitle("Notifiation from ${Constants.AppName}")
            .setContentText("Service Updated")
            .setStyle(
                NotificationCompat.BigTextStyle()
                .bigText("One service has been updated click to have a look"))
            .setContentIntent(servicePendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManagerCompat =  NotificationManagerCompat.from(this)
        val id = Random().nextInt()

        notificationManagerCompat.notify(id, builder.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }



    val adminNotificationListener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val key =  snapshot.key
            Log.d(TAG, "onAdminChildAdded: ${key}")
            val notifications = snapshot.getValue<Notifications>()
            if(notifications!=null) openNotification(notifications.takenServiceId, notifications.createdById)
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            val key =  snapshot.key
            Log.d(TAG, "onAdminChildChanged: ${key}")
            val notifications = snapshot.getValue<Notifications>()
            if(notifications!=null) openNotification(notifications.takenServiceId, notifications.createdById)
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            val key =  snapshot.key
            Log.d(TAG, "onAdminChildRemoved: ${key}")
            val notifications = snapshot.getValue<Notifications>()
            if(notifications!=null) openNotification(notifications.takenServiceId, notifications.createdById)
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            val key =  snapshot.key
            Log.d(TAG, "onAdminChildMoved: ${key}")
            val notifications = snapshot.getValue<Notifications>()
            if(notifications!=null) openNotification(notifications.takenServiceId, notifications.createdById)
        }

        override fun onCancelled(error: DatabaseError) {
            val key =  error.message
            Log.d(TAG, "onAdminChildErrror: ${key}")
            //openNotification(notifications.takenServiceId, notifications.createdById)
        }


    }

    fun listenforAdminNotifications(){
        DbInstance.getDbInstance().reference.child(Constants.Notifications)
            .child(Constants.admin)
            .addChildEventListener(adminNotificationListener)
    }



    val engineerNotificationListener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val key =  snapshot.key
            Log.d(TAG, "onEngineerChildAdded: ${key}")
            val notifications = snapshot.getValue<Notifications>()
            if(notifications!=null) openNotification(notifications.takenServiceId, notifications.createdById)
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            val key =  snapshot.key
            Log.d(TAG, "onEngineerChildChanged: ${key}")
            val notifications = snapshot.getValue<Notifications>()
            if(notifications!=null) openNotification(notifications.takenServiceId, notifications.createdById)
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            val key =  snapshot.key
            Log.d(TAG, "onEngineerChildRemoved: ${key}")
            val notifications = snapshot.getValue<Notifications>()
            if(notifications!=null) openNotification(notifications.takenServiceId, notifications.createdById)
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            val key =  snapshot.key
            Log.d(TAG, "onEngineerChildMoved: ${key}")
            val notifications = snapshot.getValue<Notifications>()
            if(notifications!=null) openNotification(notifications.takenServiceId, notifications.createdById)
        }

        override fun onCancelled(error: DatabaseError) {
            val key =  error.message
            Log.d(TAG, "onEngineerChildErrror: ${key}")
        }


    }

    fun listenforEngineerNotifications(){
        DbInstance.getDbInstance().reference.child(Constants.Notifications)
            .child(Constants.engineer)
            .child(DbInstance.getUserUid(applicationContext))
            .child(Constants.NotificationFor)
            .addChildEventListener(engineerNotificationListener)
    }


    val userNotificationListener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val key =  snapshot.key
            Log.d(TAG, "onUserChildAdded: ${key}")
            val notifications = snapshot.getValue<Notifications>()
            if(notifications!=null) openNotification(notifications.takenServiceId, notifications.createdById)
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            val key =  snapshot.key
            Log.d(TAG, "onUserChildChanged: ${key}")
            val notifications = snapshot.getValue<Notifications>()
            if(notifications!=null) openNotification(notifications.takenServiceId, notifications.createdById)
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            val key =  snapshot.key
            Log.d(TAG, "onUserChildRemoved: ${key}")
            val notifications = snapshot.getValue<Notifications>()
            if(notifications!=null) openNotification(notifications.takenServiceId, notifications.createdById)
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            val key =  snapshot.key
            Log.d(TAG, "onUserChildMoved: ${key}")
            val notifications = snapshot.getValue<Notifications>()
            if(notifications!=null) openNotification(notifications.takenServiceId, notifications.createdById)
        }

        override fun onCancelled(error: DatabaseError) {
            val key =  error.message
            Log.d(TAG, "onUserChildErrror: ${key}")
        }


    }

    fun listenforUserNotifications(){
        DbInstance.getDbInstance().reference.child(Constants.Notifications)
            .child(Constants.user)
            .child(DbInstance.getUserUid(applicationContext))
            .child(Constants.NotificationFor)
            .addChildEventListener(userNotificationListener)
    }

}