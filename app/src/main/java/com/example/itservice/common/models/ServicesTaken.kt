package com.example.itservice.common.models

data class ServicesTaken(var id: String? = null,
                         var status: String? = null,
                         var createdByID: String? =null,
                         var createdByName: String? = null,
                         var assignedEngineerName: String? = null,
                         var assignedEngineerID: String? = null,
                         var brandID: String? = null,
                         var brandName: String? = null,
                         var serviceID: String? = null,
                         var serviceName: String? = null,
                         var catagoryID: String? = null,
                         var modelName: String? = null,
                         var catagoryName: String? = null,
                         var serviceCost: String? = null,
                         var problemStatement: String? = null,
                         var parts: ArrayList<Parts>? = null
                    )
