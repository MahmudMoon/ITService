package com.example.itservice.common.models

data class Offers(
    var id: String? = null,
    val title: String? = null,
    val productID: String? = null,
    val imageUrl: String? = null,
    val previousPrice: Int? = null,
    val newPrice: Int? = null,
    val catagoryId: String? = null ,
    val details: String? = null)