package com.example.itservice.common.taken_service_catagory.service_list

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.itservice.common.taken_service_catagory.service_list.service_modify.ServiceModifyActivity
import com.example.itservice.common.models.ServicesTaken
import com.example.itservice.common.utils.Constants
import com.example.itservice.databinding.ServiceListItemBinding
import org.w3c.dom.Text

class ServiceListAdapter(val context: Context, private val productItems: List<ServicesTaken>) : RecyclerView.Adapter<ServiceListAdapter.ProductViewHolder>() {
    class ProductViewHolder(binding: ServiceListItemBinding): RecyclerView.ViewHolder(binding.root){
        val tvID: TextView = binding.tvIdServiceListAdapter
        val tvStatus: TextView = binding.tvStatusServiceListAdapter
        val tvServiceModel: TextView = binding.tvModelServiceListAdapter
        val tvAssignedTo: TextView = binding.tvAssignedToServiceListAdapter
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ServiceListItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.tvID.text = "ID: "+ productItems[position].id
        holder.tvStatus.text = "Status: "+ productItems[position].status
        holder.tvServiceModel.text = "Model: " +productItems[position].modelName
        holder.tvAssignedTo.text = "Assigned To: " +productItems[position].assignedEngineerName
        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context, ServiceModifyActivity::class.java)
                .putExtra(Constants.takenServiceId, productItems[position].id)
                .putExtra(Constants.userID, productItems[position].createdByID))
            (context as ServiceListActivity).onServiceSelected()
        }

    }

    override fun getItemCount(): Int {
        return productItems.size
    }
}