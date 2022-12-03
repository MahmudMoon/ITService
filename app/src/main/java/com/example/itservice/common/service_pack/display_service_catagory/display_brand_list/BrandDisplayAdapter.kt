package com.example.itservice.common.service_pack.display_service_catagory.display_brand_list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.itservice.common.models.Brand
import com.example.itservice.databinding.ServiceCatItemBinding

class BrandDisplayAdapter(val context: Context, var catList: List<Brand>) : RecyclerView.Adapter<BrandDisplayAdapter.CatagoryHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatagoryHolder {
        val inflater = LayoutInflater.from(context)
        val binding = ServiceCatItemBinding.inflate(inflater, parent, false)
        return CatagoryHolder(binding)
    }

    override fun onBindViewHolder(holder: CatagoryHolder, position: Int) {
        val brand = catList.get(position)
        holder.tvBrandName.setText(brand.name)
        holder.imageView.load(brand.brandImage)
        holder.itemView.setOnClickListener {
            (context as BrandListDisplayActivity).onBankItemclick(brand)
        }
    }

    override fun getItemCount(): Int {
        return catList.size
    }

    class CatagoryHolder(binding: ServiceCatItemBinding): RecyclerView.ViewHolder(binding.root){
        val tvBrandName = binding.tvBaseItemName
        val imageView = binding.ivBaseItemView
    }
}