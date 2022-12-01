package com.example.itservice.admin.all_list_display

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.itservice.R
import com.example.itservice.databinding.ActivityListDisplayBinding

class ListDisplayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListDisplayBinding
    private lateinit var tvListType: TextView
    private lateinit var rvDisplayList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tvListType = binding.tvListType
        rvDisplayList = binding.rvDisplayList
        // use generic adapter

        //swipe to delete

        // for add or update move to Product Modify Activity
        // for add or update move to Service Modify Activity
        // for add or update move to Offer Modify Activity
        // for add or update move to Parts Modify Activity

        // option menu will hold the add functionality for different list item




    }
}