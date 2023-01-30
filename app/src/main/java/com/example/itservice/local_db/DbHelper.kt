package com.example.itservice.local_db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.itservice.application.TAG
import com.example.itservice.common.models.Product
import com.example.itservice.common.utils.Constants
import java.sql.Array

class DbHelper(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase?) {
        val tableQuery = "CREATE TABLE IF NOT EXISTS " + Constants.TABLENAME +
                " ( " + Constants.COL_ONE + " INTEGER PRIMARY KEY AUTOINCREMENT , " + Constants.COL_ZERO + " TEXT , " + Constants.COL_TWO + " TEXT , " + Constants.COL_THREE + " INTEGER , " + Constants.COL_FOUR + " INTEGER , " + Constants.COL_FIVE + " INTEGER , " + Constants.COL_SIX + " TEXT , " + Constants.COL_SEVEN + " INTEGER )"
        try {
            db?.execSQL(tableQuery)
        } catch (exception: Exception) {
            Log.e(TAG, "onCreate: " + exception.localizedMessage)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS " + Constants.TABLENAME)
        onCreate(db)
    }

    fun addCartItem(product: Product): String {
        if (checkForExistance(product.id!!) != null) {
            return "Product exist in cart"
        }
        val db = writableDatabase
        val contentValue = ContentValues()
        contentValue.put(Constants.COL_ZERO, product.id)
        contentValue.put(Constants.COL_TWO, product.name)
        contentValue.put(Constants.COL_THREE, product.price)
        contentValue.put(Constants.COL_FOUR, 1)
        contentValue.put(Constants.COL_FIVE, product.quantity)
        contentValue.put(Constants.COL_SIX, product.Image)
        contentValue.put(Constants.COL_SEVEN, -1)
        val isAdded = db.insert(Constants.TABLENAME, null, contentValue)
        db.close()
        if (isAdded > 0) {
            return "Added in cart"
        } else {
            return "Failed to add in cart"
        }
    }

    fun getAllCartItems(): ArrayList<Product> {
        val db = readableDatabase
        val query = "SELECT * FROM " + Constants.TABLENAME
        val cursor = db.rawQuery(query, null)
        val productList = ArrayList<Product>()
        cursor.moveToFirst()
        if (cursor.count > 0) {
            do {
                productList.add(parseProduct(cursor))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return productList
    }

    fun getSelectedCartItems(): ArrayList<Product> {
        val db = readableDatabase
        val query = "SELECT * FROM " + Constants.TABLENAME + " WHERE " + Constants.COL_SEVEN + " = ? "
        val cursor = db.rawQuery(query, arrayOf<String>("1"))
        val productList = ArrayList<Product>()
        cursor.moveToFirst()
        if (cursor.count > 0) {
            do {
                productList.add(parseProduct(cursor))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return productList
    }

    fun checkForExistance(productID: String): Product? {
        var product: Product? = null
        val db = readableDatabase
        val query =
            "SELECT * FROM " + Constants.TABLENAME + " WHERE " + Constants.COL_ZERO + " = ? "
        val selectionArgument = arrayOf<String>(productID)
        val cursor = db.rawQuery(query, selectionArgument)
        val count = cursor.count
        cursor.moveToFirst()
        if (count > 0) {
            product = parseProduct(cursor)
        }
        cursor.close()
        db.close()
        return product
    }

    fun parseProduct(cursor: Cursor): Product {
        val productID = cursor.getString(1)
        val productName = cursor.getString(2)
        val productPrice = cursor.getInt(3)
        val productPurchased = cursor.getInt(4)
        val productQuantity = cursor.getInt(5)
        val productImageURL = cursor.getString(6)
        val isProductChecked = cursor.getInt(7)

        val product = Product(
            id = productID,
            name = productName,
            catID = null,
            catName = null,
            Image = productImageURL,
            description = null,
            quantity = productQuantity,
            price = productPrice,
            purchasedProductQuantity = productPurchased,
            isProductChecked = isProductChecked>0
        )
        return product
    }

    fun updateDb(productId: String, purchasedQuantity: Int): Boolean {
        val product = checkForExistance(productId)
        if (product != null) {
            product.purchasedProductQuantity = purchasedQuantity
        }
        val contentValue = ContentValues()
        contentValue.put(Constants.COL_FOUR, purchasedQuantity)
        val db = writableDatabase
        val updated = db.update(
            Constants.TABLENAME,
            contentValue,
            Constants.COL_ZERO + " = ? ",
            arrayOf<String>(productId)
        )
        db.close()
        if (updated > 0) {
            Log.d(TAG, "updateDb: done")
            return true
        } else {
            Log.d(TAG, "updateDb: failed")
            return false
        }
    }

    fun updateProductStatus(productId: String, isChecked: Boolean): Boolean {
        val product = checkForExistance(productId)
        if (product != null) {
            product.isProductChecked = isChecked
        }

        val contentValue = ContentValues()
        if(isChecked){
            contentValue.put(Constants.COL_SEVEN, 1)
        }else{
            contentValue.put(Constants.COL_SEVEN, -1)
        }

        val db = writableDatabase
        val updated = db.update(
            Constants.TABLENAME,
            contentValue,
            Constants.COL_ZERO + " = ? ",
            arrayOf<String>(productId)
        )
        db.close()
        if (updated > 0) {
            Log.d(TAG, "updateDb: done")
            return true
        } else {
            Log.d(TAG, "updateDb: failed")
            return false
        }
    }

    fun deleteFromTable(ids: kotlin.Array<String>): Boolean {
        val db = writableDatabase
        var deleted = true
        ids.forEach {
            try {
                val isDeleted = db.delete(
                    Constants.TABLENAME,
                    Constants.COL_ZERO + " = ? ",
                    arrayOf<String>(it)
                )
                deleted = deleted && isDeleted > 0
            } catch (e: Exception) {
                deleted = false
            }
        }
        db.close()
        if (ids.size == 0) deleted = false
        return deleted
    }

    fun deleteAllEntries(){
        val db = writableDatabase
        val query = "DELETE FROM "+ Constants.TABLENAME
        db.execSQL(query)
        db.close()
    }
}