package com.example.itservice.common.parts_pack.modify_parts

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.databinding.ActivityModifyPartsBinding

class ModifyPartsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityModifyPartsBinding
    private lateinit var viewModel: PartsModifyViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModifyPartsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, ViewModelProviderFactory())
            .get(PartsModifyViewModel::class.java)
    }
}