package com.example.itservice.common.service_pack.display_service_catagory.display_brand_list

import com.example.itservice.common.models.Brand

interface BrandClick {
    fun onBankItemclick(brand: Brand)
}