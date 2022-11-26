package com.example.itservice.user.product_catagory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.itservice.R
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.databinding.ActivityBuyOurProductsBinding

class BuyOurProductsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBuyOurProductsBinding
    private lateinit var viewModel: BuyOurProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuyOurProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(BuyOurProductViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_buy_our_products, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if(item.itemId == R.id.go_to_cart){
            Toast.makeText(applicationContext, "Moving to cart", Toast.LENGTH_SHORT).show()
        }
        return true
    }
}