package com.example.itservice.common.models

data class Catagories(var id: String? = null,
                      var name: String? = null,
                      var catImage: String? = null,
                      var productList: List<Product>? = null)
