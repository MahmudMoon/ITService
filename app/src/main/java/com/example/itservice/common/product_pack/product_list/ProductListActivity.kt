package com.example.itservice.user.product_catagory.product_list

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
import com.example.itservice.admin.products_pack.add_product.AddProductActivity
import com.example.itservice.base.BaseActivity
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.common.models.Product
import com.example.itservice.common.service_pack.display_service_catagory.display_brand_list.display_service.ServiceDisplayAdapter
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.example.itservice.databinding.ActivityProductListBinding
import com.example.itservice.user.product_catagory.product_list.product_details.ProductDetailActivity
import com.example.itservice.user.product_catagory.product_list.product_details.buy_product.CartActivity

class ProductListActivity : BaseActivity(), ProductsSelected {
    lateinit var binding: ActivityProductListBinding
    lateinit var viewModel: ProductListViewModel

    private var rvDisplayService: RecyclerView? = null
    private var rvProductsAdapter: ProductListAdapter? = null
    private var productsList = ArrayList<Product>()
    private lateinit var catId: String
    private lateinit var catName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitleForActivity("Products")
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(ProductListViewModel::class.java)

        val bundle = intent?.extras
        if(bundle!=null){
            catId = bundle.getString(Constants.CatagoryId, "")
            catName = bundle.getString(Constants.CatagoryName, "")

            viewModel.getserviceListForCatagorySelected(catId)
        }

        rvDisplayService = binding.rvProductList
        rvProductsAdapter = ProductListAdapter(this@ProductListActivity, productsList)
        rvDisplayService?.layoutManager = GridLayoutManager(this, 2)
        rvDisplayService?.adapter = rvProductsAdapter


        viewModel.productsData.observe(this){ data ->
            productsList.clear()
            productsList.addAll(data)
            rvProductsAdapter?.notifyDataSetChanged()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        if(DbInstance.getLastLoginAs(this@ProductListActivity).equals(Constants.admin)){
            menuInflater.inflate(R.menu.display_add_menu, menu)
        }else if(DbInstance.getLastLoginAs(this@ProductListActivity).equals(
                Constants.user)){
            menuInflater.inflate(R.menu.menu_buy_our_products, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if(item.itemId == R.id.go_to_cart){
            Toast.makeText(applicationContext, "Moving to cart", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@ProductListActivity, CartActivity::class.java))
            moveWithAnimationToAnotherActivity()
        }else if(item.itemId == R.id.menu_add_catagory){
            Toast.makeText(applicationContext, "Moving to Add catagory", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@ProductListActivity, AddProductActivity::class.java)
                .putExtra(Constants.CatagoryId, catId)
                .putExtra(Constants.CatagoryName, catName))
            moveWithAnimationToAnotherActivity()
        }
        return true
    }

    override fun onProductItemClicked(product: Product) {
        if(DbInstance.getLastLoginAs(this@ProductListActivity).equals(Constants.admin)){
            //go for update product

        }else if (DbInstance.getLastLoginAs(this@ProductListActivity).equals(Constants.user)) {
            //go for product detail
            startActivity(
                Intent(this@ProductListActivity, ProductDetailActivity::class.java)
                    .putExtra(Constants.CatagoryId, catId)
                    .putExtra(Constants.productID, product.id)
            )
            moveWithAnimationToAnotherActivity()
        }
    }
}

interface ProductsSelected{
    fun onProductItemClicked(product: Product)
}