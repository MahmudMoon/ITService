package com.example.itservice.user.product_catagory.product_list.product_details

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.itservice.R
import com.example.itservice.base.BaseActivity
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.common.models.Product
import com.example.itservice.common.utils.Constants
import com.example.itservice.databinding.ActivityProductDetailBinding
import com.example.itservice.local_db.DatabaseInstance
import com.example.itservice.local_db.DbHelper
import com.example.itservice.user.product_catagory.product_list.product_details.buy_product.CartActivity


class ProductDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityProductDetailBinding
    private lateinit var viewModel: ProductDetailViewModel
    private lateinit var ivProductImage: ImageView
    private lateinit var tvProductName: TextView
    private lateinit var tvProductQuantity: TextView
    private lateinit var tvProductPrice: TextView
    private lateinit var tvProductDetail: TextView
    private lateinit var tvProductOfferPrice: TextView
    private lateinit var btnAddToCart: Button
    private lateinit var btnGoToCart: Button
    private var dbHelper: DbHelper? = null
    private lateinit var product: Product
    private val STRIKE_THROUGH_SPAN = StrikethroughSpan()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitleForActivity("Details")
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
        tvProductOfferPrice = binding.tvProductQuantityProductOfferPrice
        btnAddToCart.isClickable = false
        dbHelper = DatabaseInstance.getDatabaseReference(this)

        val bundle = intent?.extras
        if(bundle!=null){
           val catID = bundle.getString(Constants.CatagoryId, "")
           val productId = bundle.getString(Constants.productID, "")
           viewModel.getSignleProduct(catId = catID, productId = productId)
        }

        viewModel.productData.observe(this){ product->
            this.product = product
            ivProductImage.load(product.Image)
            tvProductName.setText("Name: ${product.name}")
            //tvProductPrice.setText()
            tvProductQuantity.setText("Quantity: ${product.quantity}")
            tvProductDetail.setText("Details: ${product.description}")
            val priceString = "Price: ${product.price} BTD"
            if(product.offeredPrice!=null){

                tvProductPrice.setText(priceString, TextView.BufferType.SPANNABLE)
                val spannable = tvProductPrice.getText() as Spannable
                spannable.setSpan(
                    STRIKE_THROUGH_SPAN,
                    0,
                    priceString.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                tvProductOfferPrice.visibility = View.VISIBLE
                tvProductOfferPrice.setText("Offer price: "+product.offeredPrice + " BTD")
            }else{
                tvProductOfferPrice.visibility = View.GONE
                tvProductPrice.setText(priceString)
            }
            btnAddToCart.isClickable = true
        }

        btnAddToCart.setOnClickListener {
            if(product.quantity!! > 0) {
                val status = dbHelper?.addCartItem(product)
                Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this@ProductDetailActivity, "Product quantity not available", Toast.LENGTH_SHORT).show()
            }
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
            startActivity(Intent(this@ProductDetailActivity, CartActivity::class.java))
            moveWithAnimationToAnotherActivity()
        }
        return true
    }
}