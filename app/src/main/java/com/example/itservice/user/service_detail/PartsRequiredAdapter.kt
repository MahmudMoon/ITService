package com.example.itservice.user.service_detail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.itservice.common.models.Parts
import com.example.itservice.databinding.PartListItemBinding

class PartsRequiredAdapter(val context: Context, private val partList: List<Parts>): RecyclerView.Adapter<PartsRequiredAdapter.PartsViewHolder>() {

    class PartsViewHolder(binding: PartListItemBinding): RecyclerView.ViewHolder(binding.root){
        var tvPartName = binding.tvPartName
        var tvPartPrice = binding.tvPartPrice
        var tvPartsQuantity = binding.tvPartSelectedQuantity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartsViewHolder {
       val binding = PartListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return PartsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PartsViewHolder, position: Int) {
        holder.tvPartName.text = partList.get(position).partName
        holder.tvPartPrice.text = partList.get(position).partPrice.toString()
        holder.tvPartsQuantity.text = partList.get(position).partQuantity.toString()
    }

    override fun getItemCount(): Int {
      return partList.size
    }
}