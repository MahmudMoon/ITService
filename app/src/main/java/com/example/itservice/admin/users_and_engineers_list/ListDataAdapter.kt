package com.example.itservice.admin.users_and_engineers_list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.itservice.common.models.Engineer
import com.example.itservice.common.models.User
import com.example.itservice.databinding.EngineerListItemBinding
import com.example.itservice.databinding.ListItemViewBinding

class ListDataAdapter(val context: Context, private val list: List<Any>) : RecyclerView.Adapter<ListDataAdapter.ListDataHolder>() {
    class ListDataHolder(binding: ListItemViewBinding): RecyclerView.ViewHolder(binding.root){
        val tvCatagory: TextView = binding.tvCatagoryEngineerListAdapter
        val tvName: TextView = binding.tvNameEngineerListAdapter
        val tvContactNumber: TextView = binding.tvContactNumberEngineerListAdapter
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListDataHolder {
        val binding = ListItemViewBinding.inflate(LayoutInflater.from(context),parent,false)
        return ListDataHolder(binding)
    }

    override fun onBindViewHolder(holder: ListDataHolder, position: Int) {
        var value = list[position]
        if(value is Engineer){
            var catagory = value.catagory
            if(catagory==null) catagory = "Not found"
            holder.tvCatagory.text = "Catagory : "+ catagory
            holder.tvName.text = "Name : "+ value.fullName
            holder.tvContactNumber.text = "Phone : " +value.contactNumber
        }else if(value is User){
            holder.tvCatagory.text = "ID : "+ value.uid
            holder.tvName.text = "Name : "+ value.fullName
            holder.tvContactNumber.text = "Phone : " +value.contactNumber
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}