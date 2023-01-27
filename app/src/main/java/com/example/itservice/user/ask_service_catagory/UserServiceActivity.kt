package com.example.itservice.user.ask_service_catagory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.itservice.R
import com.example.itservice.base.BaseActivity
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.common.service_pack.display_service_catagory.DisplayServiceCatagoryActivity
import com.example.itservice.common.taken_service_catagory.TakenServiceCatagoryActivity
import com.example.itservice.databinding.ActivitySelectServiceCatagoryBinding

class UserServiceActivity : BaseActivity() {
    private lateinit var binding: ActivitySelectServiceCatagoryBinding
    private lateinit var viewModel: UserServiceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectServiceCatagoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(UserServiceViewModel::class.java)

        binding.llyAskForService.setOnClickListener {
        }

        binding.llyTakenService.setOnClickListener {
        }
    }
}