package com.example.itservice.common.service_pack.display_service_catagory.display_brand_list.display_service

import com.example.itservice.common.models.Service

interface ClickOnService {
    fun onServiceItemClicked(service: Service)
}