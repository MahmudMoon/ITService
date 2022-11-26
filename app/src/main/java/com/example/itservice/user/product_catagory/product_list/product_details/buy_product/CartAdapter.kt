package com.example.itservice.user.product_catagory.product_list.product_details.buy_product

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.itservice.common.models.Product
import com.example.itservice.databinding.CartListItemBinding
import com.example.itservice.databinding.ProductListItemBinding

class CartAdapter(val context: Context, private val cartItems: List<Product>)  : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    class CartViewHolder(binding: CartListItemBinding): RecyclerView.ViewHolder(binding.root){
        val imageView: ImageView = binding.ivBaseItemView
        val tvProductName: TextView = binding.tvBaseItemName
        val tvBaseItemQuantity: TextView = binding.tvBaseItemQuantity
        val btnAddCount: Button = binding.btnAddCountCart
        val btnSubstractCount: Button = binding.btnSubstractCountCart
        val btnBuyConfirm: Button = binding.btnConfirmBuy
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartListItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.tvProductName.text = cartItems[position].name
        holder.imageView.load(cartItems[position].Image)
        holder.tvBaseItemQuantity.text = cartItems.get(position).quantity
        holder.btnAddCount.setOnClickListener {

        }

        holder.btnSubstractCount.setOnClickListener {

        }

        holder.btnBuyConfirm.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }
}