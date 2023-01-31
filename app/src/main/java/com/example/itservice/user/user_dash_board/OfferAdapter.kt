package com.example.itservice.user.user_dash_board

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.example.itservice.R
import com.example.itservice.admin.offer_modify.OfferModifyActivity
import com.example.itservice.common.models.Offers
import com.example.itservice.databinding.RvAdapterOfferLayoutBinding

class OfferAdapter(val context: Context,val offers: List<Offers>):
    RecyclerView.Adapter<OfferAdapter.OfferViewHolder>() {
    class OfferViewHolder(binding: RvAdapterOfferLayoutBinding) : ViewHolder(binding.root){
        val ivProductImage: ImageView = binding.ivOfferImage
        val tvProductName: TextView = binding.tvOfferProductName
        val tvProductPrice: TextView = binding.tvOfferProductPrice
        val tvProductShortDescription: TextView = binding.tvOfferProductShortDescription
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        val binding = RvAdapterOfferLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return OfferViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        val offer = offers.get(position)
        holder.ivProductImage.load(offer.imageUrl)
        holder.tvProductName.setText(offer.title)
        holder.tvProductPrice.setText(offer.newPrice.toString()+"/=")
        holder.tvProductShortDescription.setText(offer.details)
        holder.itemView.setOnClickListener {
            if(context is UserdashboardActivity) {
                context.onOfferItemSelected(offer)
            }else if(context is OfferModifyActivity){
                context.onOfferClicked(offer)
            }
        }
    }

    override fun getItemCount(): Int {
        return offers.size
    }
}