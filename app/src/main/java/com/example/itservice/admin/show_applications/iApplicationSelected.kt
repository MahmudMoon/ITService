package com.example.itservice.admin.show_applications

import com.example.itservice.common.models.Engineer

interface iApplicationSelected {
    fun onItemSelected(engineer: Engineer)
}