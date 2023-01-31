package com.example.itservice.admin.addOffers

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itservice.R
import com.example.itservice.application.TAG
import com.example.itservice.base.BaseActivity
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.common.models.Offers
import com.example.itservice.common.models.Product
import com.example.itservice.common.utils.ContextExtentions
import com.example.itservice.databinding.ActivityAddOfferBinding
import com.example.itservice.user.product_catagory.product_list.ProductListAdapter

interface iProductSelected{
    fun onProductSelected(product: Product, position: Int)
}

class AddOfferActivity : BaseActivity(), iProductSelected {
    private lateinit var binding: ActivityAddOfferBinding
    private lateinit var viewModel: AddOfferViewModel
    private var rvProductsAdapter: ProductListAdapter? = null
    private var productsList = ArrayList<Product>()
    private var rvDisplayProducts: RecyclerView? = null
    private lateinit var etProductTitle: EditText
    private lateinit var etProductPrice: EditText
    private lateinit var etProductDescrition: EditText
    private lateinit var btnAddOffer: Button
    private lateinit var etCurrentPrice: EditText
    private lateinit var product: Product
    private lateinit var llyAddOfferContainer: CardView
    private lateinit var ivCross: ImageView
    private var price: Int? = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitleForActivity("Add Offers")
        binding = ActivityAddOfferBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(AddOfferViewModel::class.java)

        rvDisplayProducts = binding.rvProductList
        etProductDescrition = binding.etOfferDescription
        etProductPrice = binding.etOfferPrice
        etProductTitle = binding.etOfferTitle
        llyAddOfferContainer = binding.llyBottomContainer
        etCurrentPrice = binding.etOfferCurrentPrice
        btnAddOffer = binding.btnAddOffer
        ivCross = binding.ivCross

        etCurrentPrice.keyListener = null
        etProductTitle.keyListener = null

        rvProductsAdapter = ProductListAdapter(this@AddOfferActivity, productsList)
        rvDisplayProducts?.layoutManager = LinearLayoutManager(this)
        rvDisplayProducts?.adapter = rvProductsAdapter


        viewModel.getserviceListForCatagorySelected()

        viewModel.productsData.observe(this){ data ->
            productsList.clear()
            productsList.addAll(data)
            rvProductsAdapter?.notifyDataSetChanged()
            Log.d(TAG, "onCreate: productsData -> "+data.size)
        }

        viewModel.isOfferAdded.observe(this){
            if(it){
                Toast.makeText(this@AddOfferActivity, "Added an offer", Toast.LENGTH_SHORT).show()
                //change current price to offer price
                Log.d(TAG, "onCreate: price: "+price)
                if(price!=null && price!!>=0) {
                    viewModel.changeProductPrice(product.id, product.catID, price!!)
                }
            }else{
                Toast.makeText(this@AddOfferActivity, "Failed to add offer", Toast.LENGTH_SHORT).show()
            }
        }

        ivCross.setOnClickListener {
            crossClicked()
        }

        btnAddOffer.setOnClickListener {
            ContextExtentions.hideKeyboard(it, this@AddOfferActivity)
            try {
                val productName = etProductTitle.text.toString()
                val productCurrentPrice = etCurrentPrice.text.toString()
                val productNewPrice = etProductPrice.text.toString()
                val productDescription = etProductDescrition.text.toString()
                price = productNewPrice.toIntOrNull()

                if(productName.length>0 && productCurrentPrice.length>0 && productNewPrice.length>0 && productDescription.length >0 ) {
                    val offer = Offers(
                        id = null,
                        title = productName,
                        productID = product.id,
                        imageUrl = product.Image,
                        previousPrice = product.price,
                        newPrice = price,
                        catagoryId = product.catID,
                        details = productDescription
                    )
                    viewModel.addAnOffer(offer)
                    crossClicked()
                }
            }catch (e: Exception){
                crossClicked()
                Toast.makeText(this@AddOfferActivity, "Failed to add offer", Toast.LENGTH_SHORT).show()
            }

        }

    }

    fun crossClicked(){
        if(llyAddOfferContainer.visibility == View.VISIBLE){
            etCurrentPrice.setText("")
            etProductPrice.setText("")
            etProductTitle.setText("")
            etProductDescrition.setText("")
            val animationClose = AnimationUtils.loadAnimation(this@AddOfferActivity, R.anim.slide_in_bottom)
            llyAddOfferContainer.startAnimation(animationClose)
            llyAddOfferContainer.visibility = View.GONE
        }
    }

    override fun onProductSelected(product: Product, position: Int) {
        Log.d(TAG, "onProductSelected: "+product.id)
        if (llyAddOfferContainer.visibility != View.VISIBLE){
            rvDisplayProducts?.scrollToPosition(position)
            val animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_top)
            llyAddOfferContainer.startAnimation(animation)
            llyAddOfferContainer.visibility = View.VISIBLE
        }
        this.product = product
        etProductTitle.setText(product.name)
        etCurrentPrice.setText(product.price.toString())
        etCurrentPrice.setOnEditorActionListener(null)
    }
}