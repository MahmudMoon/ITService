package com.example.itservice.common.models

data class ServiceCatagory(var caragoryId: String? = null,
                           var catagoryName: String? = null,
                           var catagoryImage: String? = null,
                           var brands: List<ServiceBrand>? = null
                           )