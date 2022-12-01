package com.example.itservice.common.models

data class User(val uid: String? = null,
                val fullName: String? = null,
                val email: String? = null,
                val password: String? = null,
                val tin: String? = null,
                val nid: String? = null,
                val companyName: String? = null,
                val companyAddress: String? = null,
                val contactNumber: String? = null)
