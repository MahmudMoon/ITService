package com.example.itservice.admin.admin_dashboard

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.itservice.R
import com.example.itservice.application.TAG
import com.example.itservice.common.models.AdminItem
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class AdminDashBoradViewModel : ViewModel() {
    val itemNameAry = arrayListOf<String>(
        "Check Tasks",
        "Add Products",
        "Add Service",
        "Add Parts",
        "Add Offers",
        "Users",
        "Engineers",
        "Edit offers",
        "Register Engineers"
    )

    val itemImageAry = arrayListOf<Int>(
        R.drawable.check_task,
        R.drawable.add_product,
        R.drawable.create_service,
        R.drawable.add_parts,
        R.drawable.add_offer,
        R.drawable.user_list,
        R.drawable.engineer_list,
        R.drawable.edit_offers,
        R.drawable.engineer_list,
    )

    fun getDashBoardItems(): ArrayList<AdminItem> {
        val itemLists = ArrayList<AdminItem>()
        for (i in 0..itemImageAry.size - 1) {
            itemLists.add(AdminItem(itemNameAry.get(i), itemImageAry.get(i)))
        }
        return itemLists
    }
}