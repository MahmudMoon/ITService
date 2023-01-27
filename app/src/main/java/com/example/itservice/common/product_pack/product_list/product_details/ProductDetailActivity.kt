package com.example.itservice.user.product_catagory.product_list.product_details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.itservice.R
import com.example.itservice.base.BaseActivity
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.common.utils.Constants
import com.example.itservice.databinding.ActivityProductDetailBinding

class ProductDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityProductDetailBinding
    private lateinit var viewModel: ProductDetailViewModel
    private lateinit var ivProductImage: ImageView
    private lateinit var tvProductName: TextView
    private lateinit var tvProductQuantity: TextView
    private lateinit var tvProductPrice: TextView
    private lateinit var tvProductDetail: TextView
    private lateinit var btnAddToCart: Button
    private lateinit var btnGoToCart: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(ProductDetailViewModel::class.java)
        ivProductImage = binding.ivProductImageProductDetail
        tvProductName = binding.tvProductNameProductDetail
        tvProductQuantity = binding.tvProductQuantityProductDetail
        tvProductPrice = binding.tvProductQuantityProductPrice
        tvProductDetail = binding.tvProductDescriptionProductDetail
        btnAddToCart = binding.btnAddToCartProductDetail
        btnGoToCart = binding.btnGoToCartProductDetail

        val bundle = intent?.extras
        if(bundle!=null){
           val catID = bundle.getString(Constants.CatagoryId, "")
           val productId = bundle.getString(Constants.productID, "")
           viewModel.getSignleProduct(catId = catID, productId = productId)
        }

        viewModel.productData.observe(this){ product->
            ivProductImage.load(product.Image)
            tvProductName.setText("Name: ${product.name}")
            tvProductPrice.setText("Price: ${product.price} BTD")
            tvProductQuantity.setText("Quantity: ${product.quantity}")
            tvProductDetail.setText("Details: ${product.description}")
        }
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