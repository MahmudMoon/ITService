package com.example.itservice.admin.admin_dashboard

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.itservice.common.models.AdminItem
import com.example.itservice.databinding.AdminDashBoardItemBinding

class AdminListAdapter(val context: Context, val items: ArrayList<AdminItem>):
    RecyclerView.Adapter<AdminListAdapter.ItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
       val binding = AdminDashBoardItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ItemHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = items[position]
        holder.tvItemName.text = item.name
        holder.ivItemIcon.setImageResource(item.image)
        holder.itemView.setOnClickListener {
            (context as AdminDashBoardActivity).adminItemClickedAt(position)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }


    class ItemHolder(itemBinding: AdminDashBoardItemBinding): RecyclerView.ViewHolder(itemBinding.root){
        val tvItemName = itemBinding.tvBaseItemName
        val ivItemIcon = itemBinding.ivBaseItemView
    }
}