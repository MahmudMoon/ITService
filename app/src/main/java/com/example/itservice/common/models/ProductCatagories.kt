package com.example.itservice.common.models

data class ProductCatagories(var id: String? = null,
                             var name: String? = null,
                             var catImage: String? = null,
                             var brandList: List<Brand>? = null)
