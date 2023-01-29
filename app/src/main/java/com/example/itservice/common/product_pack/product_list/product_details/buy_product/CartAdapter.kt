package com.example.itservice.user.product_catagory.product_list.product_details.buy_product

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.itservice.application.TAG
import com.example.itservice.common.models.Cart
import com.example.itservice.common.models.Product
import com.example.itservice.common.utils.Constants
import com.example.itservice.databinding.CartListItemBinding

class CartAdapter(val context: Context, private val cartItems: List<Product>)  : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    class CartViewHolder(binding: CartListItemBinding): RecyclerView.ViewHolder(binding.root){
        val imageView: ImageView = binding.ivBaseItemView
        val tvProductName: TextView = binding.tvBaseItemName
        val tvBaseItemQuantity: TextView = binding.tvBaseItemQuantity
        val incrementOne: ImageButton = binding.btnAddCountCart
        val decrementOne: ImageButton = binding.btnSubstractCountCart
        val checkBox: CheckBox = binding.cbSelectProduct
        val tvItemPrice: TextView = binding.tvBaseItemPrice

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartListItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val product = cartItems[position]
        holder.tvProductName.text = product.name
        holder.imageView.load(product.Image)
        holder.tvItemPrice.text = "" + product.price + " TK"
        holder.tvBaseItemQuantity.text = "Quantity: "+product.purchasedProductQuantity.toString()
        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = product.isProductChecked!!

        if(product.isProductChecked!!){
            val purchasedQuantity = product.purchasedProductQuantity
            val productPrice = product.price
            if(productPrice!=null && purchasedQuantity!=null){
                val subTotal = productPrice * purchasedQuantity
                (context as CartActivity).addToSubTotal(subTotal)
            }
        }


        holder.incrementOne.setOnClickListener {
            var purchasedQuantity = product.purchasedProductQuantity
            val quantity = product.quantity
            Log.d(TAG, "onBindViewHolder: "+product.quantity)
            if (purchasedQuantity != null && quantity != null) {
                if(purchasedQuantity < quantity){
                    purchasedQuantity+=1
                    cartItems[position].purchasedProductQuantity = purchasedQuantity
                    holder.tvBaseItemQuantity.text = "Quantity: "+product.purchasedProductQuantity.toString()
                    (context as CartActivity).updateDatabase(product.id!!, purchasedQuantity)
                    Log.d(TAG, "onBindViewHolder: inc")
                    if(holder.checkBox.isChecked){
                        val productPrice = product.price
                        if(productPrice!=null) {
                            val subTotal = productPrice
                            (context as CartActivity).addToSubTotal(subTotal)
                        }
                    }
                }else{
                    Toast.makeText(context, "Required number of products not available", Toast.LENGTH_SHORT).show()
                }
            }
        }
        holder.decrementOne.setOnClickListener {
            var purchasedQuantity = product.purchasedProductQuantity
            val quantity = product.quantity
            if (purchasedQuantity != null && quantity != null) {
                if(purchasedQuantity > 1){
                    purchasedQuantity-=1
                    cartItems[position].purchasedProductQuantity = purchasedQuantity
                    holder.tvBaseItemQuantity.text = "Quantity: "+product.purchasedProductQuantity.toString()
                    (context as CartActivity).updateDatabase(product.id!!, purchasedQuantity)
                    Log.d(TAG, "onBindViewHolder: dec")
                    if(holder.checkBox.isChecked){
                        val productPrice = product.price
                        if(productPrice!=null) {
                            val subTotal = productPrice
                            (context as CartActivity).substactFromSubTotal(subTotal)
                        }
                    }
                }
            }else{
                Toast.makeText(context, "Product quantity should be more than one", Toast.LENGTH_SHORT).show()
            }
        }
        holder.checkBox.setOnCheckedChangeListener { compoundButton, status ->
            val purchasedQuantity = product.purchasedProductQuantity
            val productPrice = product.price
                if(productPrice!=null && purchasedQuantity!=null) {
                        val subTotal = productPrice * purchasedQuantity
                    Log.d(TAG, "onBindViewHolder: subtotal " + subTotal)
                    if(status){
                        (context as CartActivity).addToSubTotal(subTotal)
                        context.storeProductIDs(product.id!!, Constants.CartStatus.Selected)
                    }else{
                        (context as CartActivity).substactFromSubTotal(subTotal)
                        context.storeProductIDs(product.id!!, Constants.CartStatus.Deselected)
                    }
                }

        }
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }
}