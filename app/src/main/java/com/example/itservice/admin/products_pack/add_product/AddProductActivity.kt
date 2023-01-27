package com.example.itservice.admin.products_pack.add_product

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.ViewModelProvider
import com.example.itservice.application.TAG
import com.example.itservice.base.BaseActivity
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.common.models.Product
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.ContextExtentions
import com.example.itservice.common.utils.PhotoUpload
import com.example.itservice.databinding.ActivityAddProductBinding
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textview.MaterialTextView

class AddProductActivity : BaseActivity(), TextWatcher {
    private lateinit var binding: ActivityAddProductBinding
    private lateinit var viewModel: AddProductViewModel

    private lateinit var etProductName: AppCompatEditText
    private lateinit var etServiceCatagory: AppCompatEditText
    private lateinit var etProductPrice: AppCompatEditText
    private lateinit var etProductQuantity: AppCompatEditText
    private lateinit var etProductDescription: AppCompatEditText
    private lateinit var btnAddImage: Button
    private lateinit var btnAddProduct: Button

    private lateinit var catId: String
    private lateinit var catName: String
    private lateinit var brandId: String
    private lateinit var brandName: String
    private var productName: String? = null
    private var productPrice: String? = null
    private var productQuantity: String? = null
    private var productImage: String? = null
    private var productDescription: String? = null
    private var tvImageName: TextView? = null
    private val IMAGE_PICK_REQUEST = 126
    var filePath: Uri? = null
    private var photoUpload: PhotoUpload? = null
    private lateinit var productID: String
    private lateinit var progressBar: ProgressBar
    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(AddProductViewModel::class.java)


        etProductName = binding.etProductNameAddProduct
        etProductQuantity = binding.etProductQuantityAddProduct
        etProductPrice = binding.etProductPriceAddProduct
        etProductDescription = binding.etDescriptionAboutProductAddProduct
        tvImageName = binding.tvSelectImageAddProduct
        btnAddImage = binding.btnSelectImageAddProduct
        btnAddProduct = binding.btnSubmitProductAddProduct
        progressBar = binding.pbAddProduct
        etServiceCatagory = binding.etProductCatagoryAddProduct

        //cat id, cat name, brand id, brand name
        val bundle = intent?.extras
        if(bundle!=null){
            catId = bundle.getString(Constants.CatagoryId,"")
            catName = bundle.getString(Constants.CatagoryName,"")
            etServiceCatagory.setText(catName)
            disableEditText(etServiceCatagory)
           // etServiceCatagory.setText(catName)
        }

        binding.btnSubmitProductAddProduct.setOnClickListener {
            ContextExtentions.hideKeyboard(it, this@AddProductActivity)
            productName = setErrorMessage(etProductName, "Enter Valid name")
            productPrice = setErrorMessage(etProductPrice, "Enter Valid Price")
            productQuantity = setErrorMessage(etProductQuantity, "Enter Valid Quantity")
            productDescription = setErrorMessage(etProductDescription, "Enter Valid Description")
            productImage = setErrorMessage(tvImageName, "Please select an image")

            if (productName != "" && productImage!=null && filePath != null
                && productPrice != "" && productQuantity != ""  && productDescription != null) {
                //store data in db first then put in realtime db
                progressBar.visibility = View.VISIBLE
                productID =  viewModel.getNewKey()!!
                photoUpload?.uploadImageInFireStore(productID, filePath.toString(), viewModel.uploadPhoto)
            }
        }

        viewModel.uploadPhoto.observe(this){ path ->
            Log.d(TAG, "onCreate: Photo uploaded ${path}")
            if(path!=""){
                val product =
                    Product(id = null,
                            name = productName,
                            catID = catId,
                            catName = catName,
                            Image = path,
                            description = productDescription,
                            quantity = productQuantity?.toIntOrNull(),
                            price = productPrice?.toIntOrNull())
                val productID = viewModel.getNewKey()
                product.id = productID
                viewModel.storeserviceDataInFirebase(catId, product)
            }
        }

        viewModel.productDataAdded.observe(this){ dbresult ->
            if(dbresult.isSuccess){
                progressBar.visibility = View.GONE
                etProductName?.setText("")
                tvImageName?.setText("")
                etProductPrice?.setText("")
                tvImageName?.setText("")
                etProductQuantity.setText("")
                etProductDescription.setText("")

                Toast.makeText(this@AddProductActivity, "One service added", Toast.LENGTH_SHORT).show()
            }else{
                progressBar.visibility = View.GONE
                Toast.makeText(this@AddProductActivity, "Failed to add service", Toast.LENGTH_SHORT).show()

            }
        }

        binding.btnSelectImageAddProduct.setOnClickListener {
            tvImageName?.setError(null)
            //pick an image
            photoUpload = PhotoUpload(this@AddProductActivity)
            photoUpload?.selectImage(IMAGE_PICK_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == IMAGE_PICK_REQUEST
            && data != null
            && data.getData() != null){
            filePath = data.getData();
            Log.d("TAG", "onActivityResult: $filePath")
            if(filePath!=null && filePath?.path?.length!! >0){
                val paths = filePath?.path?.split("/")
                val imageName = paths?.get(paths.size-1)
                tvImageName?.text = imageName
            }
        }
    }

    fun setErrorMessage(view: TextView?, message: String): String? {
        try {
            val data = view?.text.toString()
            if (data == "" ) {
                view?.error = message
                return data
            }
            return data
        } catch (exception: Exception) {
            view?.error = message
        }
        return null
    }

    fun cleanErrorMessage(view: AppCompatEditText?) {
        try {
            view?.error = null
        } catch (exception: Exception) {
            view?.error = null
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun afterTextChanged(p0: Editable?) {
        when (p0.hashCode()) {
            etProductName?.text.hashCode() -> cleanErrorMessage(etProductName)
            etProductPrice?.text.hashCode() -> cleanErrorMessage(etProductPrice)
            etProductQuantity?.text.hashCode() -> cleanErrorMessage(etProductQuantity)
            etProductDescription?.text.hashCode() -> cleanErrorMessage(etProductDescription)
        }
    }


    fun disableEditText(view: AppCompatEditText){
        view.isEnabled = false
        view.inputType = InputType.TYPE_NULL
    }
}