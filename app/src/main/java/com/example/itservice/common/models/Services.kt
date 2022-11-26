package com.example.itservice.common.models

data class Services(var id: String? = null,
                    var status: String? = null,
                    var assignedEngineerName: String? = null,
                    var assignedEngineerID: String? = null,
                    var brandName: String? = null,
                    var modelName: String? = null,
                    var catagory: String? = null,
                    var serviceCost: String? = null,
                    var problemStatement: String? = null
                    )
