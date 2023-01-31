package com.example.itservice.common.utils

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.itservice.application.TAG

class RVItemTouchHelperProvider(val isDeleted: MutableLiveData<Int>) {
    fun getItemTouchHelper(): ItemTouchHelper.SimpleCallback {
       val listener = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.UP){
           override fun onMove(
               recyclerView: RecyclerView,
               viewHolder: RecyclerView.ViewHolder,
               target: RecyclerView.ViewHolder
           ): Boolean {
               Log.d(TAG, "onMove: ")
               return false
           }

           override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
               if(direction == ItemTouchHelper.UP){
                   Log.d(TAG, "onSwiped: UP")
                   isDeleted.postValue(viewHolder.adapterPosition)
                   return
               }else{
                   Log.d(TAG, "onSwiped: others")
                   isDeleted.postValue(-1)
                   return
               }
           }

       }
        return listener
    }
}