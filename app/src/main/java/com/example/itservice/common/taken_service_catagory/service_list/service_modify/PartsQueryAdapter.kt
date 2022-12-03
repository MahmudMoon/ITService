package com.example.itservice.common.taken_service_catagory.service_list.service_modify

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.itservice.R
import com.example.itservice.common.models.Parts
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.example.itservice.databinding.PartsQueryListItemBinding

class PartsQueryAdapter(val context: Context, private val partList: List<Parts>): RecyclerView.Adapter<PartsQueryAdapter.PartsViewHolder>() {

    class PartsViewHolder(binding: PartsQueryListItemBinding): RecyclerView.ViewHolder(binding.root){
        var tvPartName = binding.tvPartName
        var tvPartPrice = binding.tvPartPrice
        var tvPartsQuantity = binding.tvPartQuantity
        var ibtnTick = binding.ibtnAccept
        var ibtnCross = binding.ibtnDecline
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartsViewHolder {
       val binding = PartsQueryListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return PartsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PartsViewHolder, position: Int) {
        holder.tvPartName.text = partList.get(position).partName
        holder.tvPartPrice.text = partList.get(position).partPrice.toString()
        holder.tvPartsQuantity.text = partList.get(position).partQuantity.toString()
        if(DbInstance.getLastLoginAs(context).equals(Constants.admin)){
            holder.ibtnTick.visibility = View.VISIBLE
            if(partList.get(position).isAccepted){
                holder.ibtnTick.setImageDrawable(context.getDrawable(R.drawable.tick_green))
                holder.ibtnCross.isEnabled = false
                holder.ibtnTick.isEnabled = false
                holder.ibtnCross.setImageDrawable(context.getDrawable(R.drawable.cross_gray))
            }
            holder.ibtnCross.visibility = View.VISIBLE
        }

        holder.ibtnTick.setOnClickListener {
            (context as ServiceModifyActivity).onTickClickedByAdmin(partList.get(position).partID!!, position)
        }
        holder.ibtnCross.setOnClickListener {
            (context as ServiceModifyActivity).onCrossClickedByAdmin(partList.get(position).partID!!, position )
        }
    }

    override fun getItemCount(): Int {
      return partList.size
    }
}