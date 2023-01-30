package com.example.itservice.user.product_catagory.product_list

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.itservice.admin.addOffers.AddOfferActivity
import com.example.itservice.common.models.Product
import com.example.itservice.databinding.ProductListItemBinding

class ProductListAdapter(val context: Context, private val productItems: List<Product>) : RecyclerView.Adapter<ProductListAdapter.ProductViewHolder>() {
    class ProductViewHolder(binding: ProductListItemBinding): RecyclerView.ViewHolder(binding.root){
        val imageView: ImageView = binding.ivBaseItemView
        val textView: TextView = binding.tvBaseItemName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ProductListItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.textView.text = productItems[position].name
        holder.imageView.load(productItems[position].Image)
        holder.itemView.setOnClickListener {
            if(context is ProductListActivity){
                context.onProductItemClicked(product = productItems.get(position))
            }else if(context is AddOfferActivity){
                context.onProductSelected(product = productItems.get(position), position)
            }

        }
    }

    override fun getItemCount(): Int {
        return productItems.size
    }
}