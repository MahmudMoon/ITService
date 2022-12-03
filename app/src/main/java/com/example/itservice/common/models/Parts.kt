package com.example.itservice.common.models

data class Parts(var partID: String? = null,
                 var partName: String? = null,
                 var partQuantity: Int? = null,
                 var partPrice: Int? = null,
                 var partAvailbleAfterRequest: Int? = null,
                 var isAccepted: Boolean = false
                 )
