package com.example.itservice.admin.products_pack.add_product_catagories

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.ViewModelProvider
import com.example.itservice.R
import com.example.itservice.application.TAG
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.common.models.ProductCatagories
import com.example.itservice.common.models.ServiceCatagory
import com.example.itservice.common.utils.ContextExtentions
import com.example.itservice.common.utils.PhotoUpload
import com.example.itservice.databinding.ActivityAddProductCatagoryBinding
import com.google.android.material.textview.MaterialTextView

class AddProductCatagoryActivity : AppCompatActivity(), TextWatcher {
    private lateinit var binding: ActivityAddProductCatagoryBinding
    private lateinit var viewModel: AddProductCatagoryViewModel
    private var catagoryName: String? = null
    private var etCatagoryName: AppCompatEditText? = null
    private var catagoryImage: String? = null
    private var tvCatagoryImage: MaterialTextView? = null
    private val IMAGE_PICK_REQUEST = 130
    var filePath: Uri? = null
    private var photoUpload: PhotoUpload? = null
    private lateinit var catID: String
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductCatagoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(AddProductCatagoryViewModel::class.java)
        etCatagoryName = binding.etCatagoryNameAddCatagorys
        tvCatagoryImage = binding.tvCatagoryImageName
        progressBar = binding.pbAddCatagory

        binding.btnSubmitcatagoryCatagoryAdd.setOnClickListener {
            ContextExtentions.hideKeyboard(it, this@AddProductCatagoryActivity)
            catagoryName = setErrorMessage(etCatagoryName, "Enter Valid name")
            catagoryImage = setErrorMessage(tvCatagoryImage, "Please select an image")
            if (catagoryName != "" && catagoryImage!=null && filePath != null ) {
                //store data in db first then put in realtime db
                progressBar.visibility = View.VISIBLE
                catID =  viewModel.getNewKey()!!
                photoUpload?.uploadImageInFireStore(catID, filePath.toString(), viewModel.uploadPhoto)
            }
        }

        viewModel.uploadPhoto.observe(this){ path ->
            Log.d(TAG, "onCreate: Photo uploaded ${path}")
            if(path!=""){
                val productCatagories = ProductCatagories(catID , catagoryName, path)
                viewModel.storeCatagoryDataInFirebase(productCatagories)
            }
        }

        viewModel.catagoryDataAdded.observe(this){ dbresult ->
            if(dbresult.isSuccess){
                progressBar.visibility = View.GONE
                etCatagoryName?.setText("")
                tvCatagoryImage?.setText("")
                Toast.makeText(this@AddProductCatagoryActivity, "One catagory added", Toast.LENGTH_SHORT).show()
            }else{
                progressBar.visibility = View.GONE
                Toast.makeText(this@AddProductCatagoryActivity, "Failed to add catagory", Toast.LENGTH_SHORT).show()

            }
        }

        binding.btnAddImage.setOnClickListener {
            tvCatagoryImage?.setError(null)
            //pick an image
            photoUpload = PhotoUpload(this@AddProductCatagoryActivity)
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
                tvCatagoryImage?.text = imageName
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
            etCatagoryName?.text.hashCode() -> cleanErrorMessage(etCatagoryName)
        }
    }
}