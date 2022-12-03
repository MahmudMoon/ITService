package com.example.itservice.common.parts_pack.display_parts.available_parts_fragment

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.itservice.application.TAG
import com.example.itservice.common.display_parts.DisplayPartsActivity
import com.example.itservice.common.models.Parts
import com.example.itservice.common.taken_service_catagory.service_list.service_modify.ServiceModifyActivity
import com.example.itservice.databinding.PartListItemBinding

class PartsAvailableAdapter(val context: Context, private val partList: List<Parts>): RecyclerView.Adapter<PartsAvailableAdapter.PartsViewHolder>() {

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
        holder.itemView.setOnClickListener {
            Log.d(TAG, "onBindViewHolder: part id: ${partList.get(position).partID}")
            if (context is ServiceModifyActivity){
                (context as ServiceModifyActivity).onPartSelected(partList.get(position))
            }else if(context is DisplayPartsActivity ){
                (context as DisplayPartsActivity).onPartUpdate(partList.get(position).partID!!)
            }
        }
    }

    override fun getItemCount(): Int {
      return partList.size
    }
}