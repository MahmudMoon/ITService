package com.example.itservice.common.service_pack.display_service_catagory

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.itservice.common.service_pack.display_service_catagory.display_brand_list.BrandListDisplayActivity
import com.example.itservice.common.models.ServiceCatagory
import com.example.itservice.common.utils.Constants
import com.example.itservice.databinding.ServiceCatItemBinding

class CatagoryDisplayAdapter(val context: Context, var catList: List<ServiceCatagory>) : RecyclerView.Adapter<CatagoryDisplayAdapter.CatagoryHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatagoryHolder {
        val inflater = LayoutInflater.from(context)
        val binding = ServiceCatItemBinding.inflate(inflater, parent, false)
        return CatagoryHolder(binding)
    }

    override fun onBindViewHolder(holder: CatagoryHolder, position: Int) {
        val cat = catList.get(position)
        holder.tvCatName.setText(cat.catagoryName)
        holder.imageView.load(cat.catagoryImage)
        holder.itemView.setOnClickListener {
            context.startActivity(
                Intent(context, BrandListDisplayActivity::class.java)
                    .putExtra(Constants.CatagoryId, cat.caragoryId)
                    .putExtra(Constants.CatagoryName, cat.catagoryName))
            (context as DisplayServiceCatagoryActivity).onServiceSelected()
        }

    }

    override fun getItemCount(): Int {
        return catList.size
    }

    class CatagoryHolder(binding: ServiceCatItemBinding): RecyclerView.ViewHolder(binding.root){
        val tvCatName = binding.tvBaseItemName
        val imageView = binding.ivBaseItemView
    }
}