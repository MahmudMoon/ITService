package com.example.itservice.admin.offer_modify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.itservice.R
import com.example.itservice.base.BaseActivity
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.databinding.ActivityOfferModifyBinding

class OfferModifyActivity : BaseActivity() {
    private lateinit var binding: ActivityOfferModifyBinding
    private lateinit var viewModel: OfferModifyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOfferModifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(OfferModifyViewModel::class.java)
    }
}