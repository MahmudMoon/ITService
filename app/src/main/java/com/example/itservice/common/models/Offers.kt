package com.example.itservice.common.models

data class Offers(val id: String? = null,
                  val title: String? = null,
                  val productID: String? = null,
                  val imageUrl: String? = null,
                  val previousPrice: Int? = null,
                  val newPrice: Int? = null
                  )