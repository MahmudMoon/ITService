package com.example.itservice.user.ask_service_catagory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.itservice.R
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.databinding.ActivitySelectServiceCatagoryBinding

class SelectServiceCatagoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectServiceCatagoryBinding
    private lateinit var viewModel: SelectServiceCatgoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectServiceCatagoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(SelectServiceCatgoryViewModel::class.java)
    }
}