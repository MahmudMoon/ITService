package com.example.itservice.user.taken_service_catagory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.itservice.R
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.databinding.ActivityTakenServiceCatagoryBinding

class TakenServiceCatagoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTakenServiceCatagoryBinding
    private lateinit var viewModel: TakenServiceCatagoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTakenServiceCatagoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(TakenServiceCatagoryViewModel::class.java)
    }
}