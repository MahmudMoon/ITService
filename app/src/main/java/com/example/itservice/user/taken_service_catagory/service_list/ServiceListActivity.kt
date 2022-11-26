package com.example.itservice.user.taken_service_catagory.service_list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.itservice.R
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.databinding.ActivityServiceListBinding

class ServiceListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityServiceListBinding
    private lateinit var viewModel: ServiceListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(ServiceListViewModel::class.java)
    }
}