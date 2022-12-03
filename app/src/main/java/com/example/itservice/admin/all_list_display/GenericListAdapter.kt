package com.example.itservice.admin.all_list_display

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.itservice.common.models.*
import com.example.itservice.databinding.GenericListItemBinding

class GenericListAdapter(val context: Context, val list: List<Any>): RecyclerView.Adapter<GenericListAdapter.GenericListHolder>(){
    class GenericListHolder(itemBinding: GenericListItemBinding): RecyclerView.ViewHolder(itemBinding.root){
        val tvGenericID = itemBinding.tvIdGenericListAdapter
        val tvGenericNAme = itemBinding.tvNameGenericListAdapter
        val tvAssignedToOrCatagory = itemBinding.tvAssignedToOrCatagoryGenericListAdapter
        val tvGenericPriceOrStatus = itemBinding.tvStatusGenericOrPriceListAdapter
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericListHolder {
        val binding = GenericListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return GenericListHolder(binding)
    }

    override fun onBindViewHolder(holder: GenericListHolder, position: Int) {
        val item = list.get(position)
        if(item is ServicesTaken){
            //item is for Service modify

        }else if(item is Product){
            //item is for product modify

        }else if(item is Tasks){
            // item is for pending, assigned or completed task

        }else if(item is Offers){
            // item is for offer

        }else if(item is Parts){
            // item is for PArts

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}