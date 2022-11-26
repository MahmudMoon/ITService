package com.example.itservice.common.models

data class ServiceBrand(var brandID: String? = null,
                        var brandName: String? = null,
                        var models: List<ServiceModel>? = null
                        )