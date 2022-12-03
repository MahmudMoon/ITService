package com.example.itservice.common.taken_service_catagory.service_list.service_modify

import com.example.itservice.common.models.Parts

interface PartsSelection {
    fun onPartSelected(part: Parts)
}