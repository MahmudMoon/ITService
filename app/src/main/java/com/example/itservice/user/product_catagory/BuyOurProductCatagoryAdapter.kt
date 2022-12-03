package com.example.itservice.user.product_catagory

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.itservice.common.models.ProductCatagories
import com.example.itservice.databinding.ProductCatItemProductBinding

class BuyOurProductCatagoryAdapter(val context: Context, val catagoryItems: List<ProductCatagories>) : RecyclerView.Adapter<BuyOurProductCatagoryAdapter.CatagoryViewHolder>() {
    class CatagoryViewHolder(binding: ProductCatItemProductBinding): RecyclerView.ViewHolder(binding.root){
        val imageView: ImageView = binding.ivBaseItemView
        val textView: TextView = binding.tvBaseItemName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatagoryViewHolder {
        val binding = ProductCatItemProductBinding.inflate(LayoutInflater.from(context),parent,false)
        return CatagoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CatagoryViewHolder, position: Int) {
        holder.textView.text = catagoryItems.get(position).name
        holder.imageView.load(catagoryItems.get(position).catImage)
    }

    override fun getItemCount(): Int {
        return catagoryItems.size
    }
}