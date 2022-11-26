package com.example.itservice.user.taken_service_catagory.service_list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.itservice.common.models.Product
import com.example.itservice.common.models.Services
import com.example.itservice.databinding.ProductListItemBinding
import com.example.itservice.databinding.ServiceListItemBinding
import com.example.itservice.user.product_catagory.product_list.ProductListAdapter

class ServiceListAdapter(val context: Context, private val productItems: List<Services>) : RecyclerView.Adapter<ServiceListAdapter.ProductViewHolder>() {
    class ProductViewHolder(binding: ServiceListItemBinding): RecyclerView.ViewHolder(binding.root){
        val tvID: TextView = binding.tvIdServiceListAdapter
        val tvStatus: TextView = binding.tvIdServiceListAdapter
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ServiceListItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.tvID.text = productItems[position].id
        holder.tvStatus.text = productItems[position].status
    }

    override fun getItemCount(): Int {
        return productItems.size
    }
}