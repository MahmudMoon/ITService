package com.example.itservice.user.product_catagory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itservice.R
import com.example.itservice.admin.products_pack.add_product_catagories.AddProductCatagoryActivity
import com.example.itservice.base.BaseActivity
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.common.models.ProductCatagories
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.example.itservice.databinding.ActivityBuyOurProductsBinding
import com.example.itservice.user.product_catagory.product_list.product_details.buy_product.CartActivity

interface ProductCatagorySelection{
   fun onItemSelected()
}

class BuyOurProductsCatagoryDisplayActivity : BaseActivity(), ProductCatagorySelection {
    private lateinit var binding: ActivityBuyOurProductsBinding
    private lateinit var viewModel: BuyOurProductCatagoryDisplayViewModel
    private var proCatList = ArrayList<ProductCatagories>()
    private lateinit var rvProductCat: RecyclerView
    private lateinit var proCatAdapter: BuyOurProductCatagoryDisplayAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitleForActivity("Catagories")
        binding = ActivityBuyOurProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        rvProductCat = binding.rvCatagoryBuyOurProduct
        rvProductCat.layoutManager = GridLayoutManager(this, 2)
        proCatAdapter = BuyOurProductCatagoryDisplayAdapter(this, proCatList)
        rvProductCat.adapter = proCatAdapter

        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(BuyOurProductCatagoryDisplayViewModel::class.java)
        viewModel.getAllProductCatagories()

        viewModel.proCatData.observe(this){
            proCatList.clear()
            proCatList.addAll(it)
            proCatAdapter.notifyDataSetChanged()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        if(DbInstance.getLastLoginAs(this@BuyOurProductsCatagoryDisplayActivity).equals(Constants.admin)){
            menuInflater.inflate(R.menu.display_add_menu, menu)
        }else if(DbInstance.getLastLoginAs(this@BuyOurProductsCatagoryDisplayActivity).equals(Constants.user)){
            menuInflater.inflate(R.menu.menu_buy_our_products, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if(item.itemId == R.id.go_to_cart){
            Toast.makeText(applicationContext, "Moving to cart", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@BuyOurProductsCatagoryDisplayActivity, CartActivity::class.java))
            moveWithAnimationToAnotherActivity()
        }else if(item.itemId == R.id.menu_add_catagory){
            Toast.makeText(applicationContext, "Moving to Add catagory", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@BuyOurProductsCatagoryDisplayActivity, AddProductCatagoryActivity::class.java))
            moveWithAnimationToAnotherActivity()
        }
        return true
    }

    override fun onItemSelected() {
        moveWithAnimationToAnotherActivity()
    }

}