package com.example.itservice.common.taken_service_catagory.service_list.service_modify.search_eng_frag

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.itservice.common.models.Engineer
import com.example.itservice.common.taken_service_catagory.service_list.service_modify.ServiceModifyActivity
import com.example.itservice.common.utils.Constants
import com.example.itservice.databinding.EngineerListItemBinding
import com.example.itservice.databinding.ServiceListItemBinding

class EngineerListAdapter(val context: Context, private val engineers: List<Engineer>) : RecyclerView.Adapter<EngineerListAdapter.EngineerDataHolder>() {
    class EngineerDataHolder(binding: EngineerListItemBinding): RecyclerView.ViewHolder(binding.root){
        val tvID: TextView = binding.tvCatagoryEngineerListAdapter
        val tvName: TextView = binding.tvNameEngineerListAdapter
        val tvContactNumber: TextView = binding.tvContactNumberEngineerListAdapter
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EngineerDataHolder {
        val binding = EngineerListItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return EngineerDataHolder(binding)
    }

    override fun onBindViewHolder(holder: EngineerDataHolder, position: Int) {
        var catagory = engineers[position].catagory
        if(catagory==null){
            catagory = "Catagory: Not found"
        }
        holder.tvID.text =  catagory
        holder.tvName.text = "Name : "+ engineers[position].fullName
        holder.tvContactNumber.text = "Phone : " +engineers[position].contactNumber
        holder.itemView.setOnClickListener {
            (context as ServiceModifyActivity).onEngineerSelected(engineers[position].uid!!)
        }
    }

    override fun getItemCount(): Int {
        return engineers.size
    }
}