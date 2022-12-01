package com.example.itservice.admin.service_modify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.itservice.R
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.databinding.ActivityServiceModifyBinding

class ServiceModifyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityServiceModifyBinding
    private lateinit var viewModel: ServiceModifyViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceModifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(ServiceModifyViewModel::class.java)
    }
}