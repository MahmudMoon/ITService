package com.example.itservice.user.product_catagory.product_list.product_details.buy_product

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.itservice.R
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.databinding.ActivityCartBinding
import com.example.itservice.databinding.ActivityProductDetailBinding

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var viewModel: CartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(CartViewModel::class.java)
    }
}