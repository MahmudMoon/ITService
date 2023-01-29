package com.example.itservice.local_db

import android.content.Context
import android.util.Log
import com.example.itservice.application.TAG

class DatabaseInstance private constructor(context: Context){
    val dbVersion = 1
    val dbName = "CartDb"

    companion object{
        private var dbHelper: DbHelper? = null
        fun getDatabaseReference(context: Context): DbHelper{
            Log.d(TAG, "getDatabaseReference: called")
            if(dbHelper == null){
                Log.d(TAG, "getDatabaseReference: creating db")
                dbHelper = DatabaseInstance(context).getDatabaseInstance(context)
            }
            return dbHelper!!
        }
    }

    private fun getDb(context: Context): DbHelper {
        return DbHelper(context = context, dbName, null, dbVersion)
    }

    private fun getDatabaseInstance(context: Context): DbHelper {
        return getDb(context)
    }
}