package com.example.itservice.common.utils

object Constants {
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
    const val OFFER_LIST = "offers"

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