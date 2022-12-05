package com.example.itservice.user.product_catagory

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.itservice.common.models.ProductCatagories
import com.example.itservice.common.utils.Constants
import com.example.itservice.databinding.ProductCatItemProductBinding
import com.example.itservice.user.product_catagory.product_list.ProductListActivity

class BuyOurProductCatagoryDisplayAdapter(val context: Context, val catagoryItems: List<ProductCatagories>) : RecyclerView.Adapter<BuyOurProductCatagoryDisplayAdapter.CatagoryViewHolder>() {
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
        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context, ProductListActivity::class.java)
                .putExtra(Constants.CatagoryId, catagoryItems.get(position).id)
                .putExtra(Constants.CatagoryName,catagoryItems.get(position).name ))
        }
    }

    override fun getItemCount(): Int {
        return catagoryItems.size
    }
}