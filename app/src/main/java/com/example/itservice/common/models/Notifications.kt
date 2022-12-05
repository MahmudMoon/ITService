package com.example.itservice.common.models

data class Notifications(var notificationId: String? = null,
                         var takenServiceId: String? = null,
                         var createdById: String? = null,
                         var isRead: Boolean = false
                         )
