package com.example.itservice.admin.modify_products

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.itservice.R
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.databinding.ActivityProductListBinding
import com.example.itservice.databinding.ActivityProductModifyBinding

class ProductModifyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductModifyBinding
    private lateinit var viewModel: ProductModifyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductModifyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(ProductModifyViewModel::class.java)
    }
}