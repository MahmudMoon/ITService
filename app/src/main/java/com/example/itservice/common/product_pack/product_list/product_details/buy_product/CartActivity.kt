package com.example.itservice.user.product_catagory.product_list.product_details.buy_product

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itservice.R
import com.example.itservice.application.TAG
import com.example.itservice.base.BaseActivity
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.common.models.Product
import com.example.itservice.common.utils.Constants
import com.example.itservice.databinding.ActivityCartBinding
import com.example.itservice.local_db.DatabaseInstance
import com.example.itservice.local_db.DbHelper

interface iDataUpdated{
    fun addToSubTotal(subTotal: Int)
    fun updateDatabase(productId: String, purchasedQuantity: Int)
    fun substactFromSubTotal(subTotal: Int)
    fun storeProductIDs(id: String, status: Constants.CartStatus)
}

class CartActivity : BaseActivity(), iDataUpdated {
    private lateinit var binding: ActivityCartBinding
    private lateinit var viewModel: CartViewModel
    private lateinit var btnCheckout: Button
    private lateinit var dbHelper: DbHelper
    private lateinit var rvCartList: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private lateinit var cartList: ArrayList<Product>
    private var total: Int = 0
    private lateinit var tvSubTotal: TextView
    private var idList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbHelper = DatabaseInstance.getDatabaseReference(applicationContext)
        cartList = ArrayList()

        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(CartViewModel::class.java)
        viewModel.setDb(dbHelper)

        btnCheckout = binding.btnCardCheckout
        rvCartList = binding.rvCartListCartActivity
        tvSubTotal = binding.tvSubTotal
        cartAdapter = CartAdapter(this, cartList)
        rvCartList.layoutManager = LinearLayoutManager(this)
        rvCartList.adapter = cartAdapter

        viewModel.getAllCarts()

        btnCheckout.setOnClickListener {

        }

        viewModel.cartListLive.observe(this){
            cartList.clear()
            cartList.addAll(it)
            Log.d(TAG, "onCreate: "+cartList.size)
            cartAdapter.notifyDataSetChanged()
        }

        viewModel.selectedListLive.observe(this){
            idList.clear()
            it.forEach{
                idList.add(it.id!!)
            }
            viewModel.deleteFromCartDb(idList.toTypedArray())
        }
    }

    override fun addToSubTotal(subTotal: Int) {
        total+=subTotal
        tvSubTotal.text = total.toString() + " TAKA"
    }

    override fun substactFromSubTotal(subTotal: Int) {
        total-=subTotal
        tvSubTotal.text = total.toString() + " TAKA"
    }

    override fun storeProductIDs(id: String, status: Constants.CartStatus) {
        if(status.equals(Constants.CartStatus.Selected)){
            Log.d(TAG, "updateDatabase: add "+id)
            viewModel.updateProductCheckStatus(id, true)
        }else{
            Log.d(TAG, "updateDatabase: remove "+id)
            viewModel.updateProductCheckStatus(id, false)
        }
    }

    override fun updateDatabase(
        productId: String,
        purchasedQuantity: Int
    ) {
        viewModel.updateDb(productId, purchasedQuantity)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_delete_from_cart, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.delete_from_cart){
            total = 0
            tvSubTotal.text = total.toString()+ " TAKA"
            viewModel.deleteSelectedProducts()
        }
        return super.onOptionsItemSelected(item)
    }
}