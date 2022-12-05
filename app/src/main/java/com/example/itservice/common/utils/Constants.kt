package com.example.itservice.common.utils

object Constants {
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

    enum class ServiceStatus{
        Pending,
        Assigned,
        Completed
    }

}