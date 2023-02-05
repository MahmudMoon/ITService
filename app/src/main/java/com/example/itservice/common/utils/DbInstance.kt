package com.example.itservice.common.utils

import android.content.Context
import com.example.itservice.admin.login.AdminLoginFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage


object DbInstance {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var firebaseRealtimeDb: FirebaseDatabase

    fun getAuthInstance(): FirebaseAuth {
        firebaseAuth = Firebase.auth
        return firebaseAuth
    }
    fun getStorageInstance(): FirebaseStorage {
        firebaseStorage = Firebase.storage
        return firebaseStorage
    }
    fun getDbInstance(): FirebaseDatabase{
        firebaseRealtimeDb = FirebaseDatabase.getInstance()
        return firebaseRealtimeDb
    }

    fun getUserUid(context: Context): String {
        return SharedPrefUtils.getStringValue(context, "uid")
    }

    fun setUserUid(context: Context, uid: String){
        SharedPrefUtils.putStringValue(context, "uid", uid)
    }

    fun getUserName(context: Context): String {
        return SharedPrefUtils.getStringValue(context, "name")
    }

    fun setUserName(context: Context, name: String){
        SharedPrefUtils.putStringValue(context, "name", name)
    }

    fun getUserProfile(context: Context): String {
        return SharedPrefUtils.getStringValue(context, "profile")
    }

    fun setUserProfile(context: Context, profile: String){
        SharedPrefUtils.putStringValue(context, "profile", profile)
    }

    fun getUserPhoneNumber(context: Context): String {
        return SharedPrefUtils.getStringValue(context, "phone")
    }

    fun setUserPhoneNumber(context: Context, phone: String){
        SharedPrefUtils.putStringValue(context, "phone", phone)
    }

    fun getUserEmail(context: Context): String {
        return SharedPrefUtils.getStringValue(context, "email")
    }

    fun setUserEmail(context: Context, email: String){
        SharedPrefUtils.putStringValue(context, "email", email)
    }

    fun setOpenedForFirstTime(context: Context,isOpened: String){
        SharedPrefUtils.putStringValue(context, "isOpened", isOpened)
    }

    fun openedForFirstTime(context: Context): String{
       return SharedPrefUtils.getStringValue(context, "isOpened")
    }


    fun getLastLoginAs(context: Context): String {
        return SharedPrefUtils.getStringValue(context, Constants.lastLoginAs)
    }

    fun setLastLoginAs(context: Context, lastLogin: String){
        SharedPrefUtils.putStringValue(context, Constants.lastLoginAs, lastLogin)
    }

    fun setUserImage(context: Context, profileImage: String) {
        SharedPrefUtils.putStringValue(context,Constants.profileImage, profileImage)
    }

    fun getUserImage(context: Context): String {
       return SharedPrefUtils.getStringValue(context,Constants.profileImage)
    }

    fun getDefaultImage(): String {
        return Constants.demoPro
    }

    fun setAdminLastEmail(context: Context, adminEmail: String?) {
        SharedPrefUtils.putStringValue(context = context, Constants.adminEmail , adminEmail!! )
    }

    fun setAdminLastPassword(context: Context, adminPassword: String?) {
        SharedPrefUtils.putStringValue(context = context, Constants.adminPassword , adminPassword!! )
    }

    fun getAdminLastEmail(context: Context): String {
        return SharedPrefUtils.getStringValue(context = context, Constants.adminEmail)
    }

    fun getAdminLastPassword(context: Context) : String {
        return SharedPrefUtils.getStringValue(context = context, Constants.adminPassword)
    }

}