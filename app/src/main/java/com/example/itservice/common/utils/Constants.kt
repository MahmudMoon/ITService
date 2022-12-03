package com.example.itservice.common.utils

object Constants {
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

    enum class ServiceStatus{
        Pending,
        Assigned,
        Completed
    }

}