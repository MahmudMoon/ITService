package com.example.itservice.common.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

object SharedPrefUtils {
    private fun getMySharedPref(context: Context): SharedPreferences{
       return context.getSharedPreferences("test", MODE_PRIVATE)
    }
    fun putStringValue(context: Context, key : String, value: String){
        getMySharedPref(context).edit()
            .putString(key, value)
            .apply()
    }
    fun getStringValue(context: Context, key: String): String{
      return getMySharedPref(context)
           .getString(key, "").toString()
    }
}