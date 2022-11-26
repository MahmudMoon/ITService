package com.example.itservice.user.user_dash_board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import com.example.itservice.R
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.databinding.ActivityUserdashboardBinding

class UserdashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserdashboardBinding
    private lateinit var viewModel: UserdashboardViewModel
    private lateinit var llyBuyOurProducts: LinearLayout
    private lateinit var llyTakeOurService: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserdashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(UserdashboardViewModel::class.java)
        llyBuyOurProducts = binding.llyBuyOutProducts
        llyTakeOurService = binding.llyTakeOutService

        llyBuyOurProducts.setOnClickListener {

        }

        llyTakeOurService.setOnClickListener {

        }
    }
}