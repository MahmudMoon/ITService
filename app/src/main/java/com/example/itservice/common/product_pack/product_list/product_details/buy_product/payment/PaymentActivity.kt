package com.example.itservice.common.product_pack.product_list.product_details.buy_product.payment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.itservice.base.BaseActivity
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.databinding.ActivityPaymentBinding
import com.example.itservice.user.product_catagory.product_list.product_details.buy_product.CartViewModel

class PaymentActivity : BaseActivity() {
    lateinit var binding: ActivityPaymentBinding
    lateinit var viewModel: PayementViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitleForActivity("Payment")
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(PayementViewModel::class.java)

    }
}