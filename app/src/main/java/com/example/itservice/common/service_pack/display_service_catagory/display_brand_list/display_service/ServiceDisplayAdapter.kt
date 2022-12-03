package com.example.itservice.common.service_pack.display_service_catagory.display_brand_list.display_service

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.itservice.common.models.Service
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.example.itservice.databinding.ServiceCatItemBinding

class ServiceDisplayAdapter(val context: Context, var serviceList: List<Service>) : RecyclerView.Adapter<ServiceDisplayAdapter.ServiceHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceHolder {
        val inflater = LayoutInflater.from(context)
        val binding = ServiceCatItemBinding.inflate(inflater, parent, false)
        return ServiceHolder(binding)
    }

    override fun onBindViewHolder(holder: ServiceHolder, position: Int) {
        val service = serviceList.get(position)
        holder.tvBrandName.setText(service.serviceName)
        holder.imageView.load(service.serviceImage)
        holder.itemView.setOnClickListener {
            if(DbInstance.getLastLoginAs(context).equals(Constants.user)){
                (context as DisplayServiceListActivity).onServiceItemClicked(service)
            }else if(DbInstance.getLastLoginAs(context).equals(Constants.admin)){

            }
        }
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }

    class ServiceHolder(binding: ServiceCatItemBinding): RecyclerView.ViewHolder(binding.root){
        val tvBrandName = binding.tvBaseItemName
        val imageView = binding.ivBaseItemView
    }
}