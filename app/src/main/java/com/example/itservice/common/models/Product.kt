package com.example.itservice.common.models

data class Product(var id: String? = null,
                   var name: String? = null,
                   var catID: String? = null,
                   var catName: String? = null,
                   var Image: String? = null,
                   var description: String? = null,
                   var quantity: Int? = null,
                   var price: Int? = null,
                   var purchasedProductQuantity: Int? = null,
                   var isProductChecked: Boolean? = null
                   )
