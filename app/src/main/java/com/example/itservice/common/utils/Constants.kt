package com.example.itservice.common.utils

object Constants {
    const val adminPassword: String = "adminPassword"
    const val adminEmail: String = "adminEmail"
    const val PACKAGE_NAME = "com.example.itservice"
    const val userType = "userType"
    const val AppName = "IT SERVICE"
    const val CHANNEL_ID = "125347"
    const val admin = "Admin"
    const val user = "User"
    const val engineer = "Engineer"
    const val success = true
    const val failure = false
    const val lastLoginAs = "lastLoginAs"
    const val SERVICE_CATAGORIES = "service_catagiries"
    const val brands = "brands"
    const val CatagoryId = "CatagoryId"
    const val CatagoryName = "CatagoryName"
    const val BrandId = "BrandId"
    const val BrandName = "BrandName"
    const val SERVICE_LIST = "serviceList"
    const val ServiceId = "ServiceId"
    const val TakenSericeRoot = "Taken_Service"
    const val statusType ="statusType"
    const val takenServiceId = "takenServiceId"
    const val userID = "userID"
    const val ProblemStatement = "problemStatement"
    const val PartsRoot = "Parts"
    const val partQuantity = "partQuantity"
    const val partID = "partID"
    const val parts = "parts"
    const val Notifications = "Notifications"
    const val NotificationFor = "NotificationFor"
    const val ProductCatagories = "ProductCatagories"
    const val ProductsList = "productsList"
    const val productID = "productID"
    const val TABLENAME = "cartTable"
    const val COL_ONE = "ID"
    const val COL_ZERO = "PRODUCT_ID"
    const val COL_TWO = "PRODUCT_NAME"
    const val COL_THREE = "PRODUCT_PRICE"
    const val COL_FOUR = "PRODUCT_QUANTITY"
    const val COL_FIVE = "PRODUCT_AVAILABLE_QUANTITY"
    const val COL_SIX = "PRODUCT_IMAGE_URL"
    const val COL_SEVEN = "PRODUCT_IS_CHECKED"
    const val COL_EIGHT = "PRODUCT_CAT_ID"
    const val OFFER_LIST = "offers"
    const val ProductPrice = "price"
    const val ProductQuantity = "quantity"
    const val ProductOfferPrice = "offeredPrice"
    const val totalPrice = "TotalPrice"
    const val email = "email"
    const val password = "password"
    const val tabSelection = "tabSelection"
    const val profileImage = "profileImage"
    const val cvLink = "cvLink"
    const val appliedEngineers = "appliedEngineers"
    const val CV_PICK_REQUEST: Int = 110
    const val demoPro: String = "https://firebasestorage.googleapis.com/v0/b/itapplication-ed14e.appspot.com/o/demo_profile_pic%2Fpic_profile_demo.jpeg?alt=media&token=a9494a0a-ebed-47da-85c2-ba3a15e5190e"
    const val engUid: String = "engUid"

    enum class ServiceStatus{
        Pending,
        Assigned,
        Completed
    }

    enum class UsersType{
        user,
        engineer
    }

    enum class CartStatus{
        Selected,
        Deselected
    }
}