package com.example.itservice.user.ask_service_catagory.ask_for_service

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.itservice.R
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.databinding.ActivityAskServiceBinding

class AskServiceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAskServiceBinding
    private lateinit var viewModel: AskServiceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAskServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(AskServiceViewModel::class.java)
    }
}