package com.example.itservice.user.ask_service_catagory

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.itservice.common.models.ServiceCatagory
import com.example.itservice.databinding.ServiceCatItemBinding

class ServiceCatgoryAdapter(val context: Context, val catList: List<ServiceCatagory>)
    : RecyclerView.Adapter<ServiceCatgoryAdapter.ServiceCatagoryViewHolder>() {

        class ServiceCatagoryViewHolder(binding: ServiceCatItemBinding): RecyclerView.ViewHolder(binding.root){
            val imageView: ImageView = binding.ivBaseItemView
            val textView: TextView = binding.tvBaseItemName
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceCatagoryViewHolder {
        val binding = ServiceCatItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ServiceCatagoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ServiceCatagoryViewHolder, position: Int) {
        holder.textView.text = catList.get(position).catagoryName
        holder.imageView.load(catList.get(position).catagoryImage)
    }

    override fun getItemCount(): Int {
      return catList.size
    }
}