package com.example.itservice.admin.service_pack.add_service.add_brandagories.add_brand

import android.content.Intent
import android.net.Uri
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
import com.example.itservice.admin.service_pack.add_service.add_brandes.add_brand.AddBrandViewModel
import com.example.itservice.application.TAG
import com.example.itservice.base.BaseActivity
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.common.models.Brand
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.ContextExtentions
import com.example.itservice.common.utils.DocumentUpload
import com.example.itservice.databinding.ActivityAddBrandBinding
import com.google.android.material.textview.MaterialTextView

class AddBrandActivity : BaseActivity() , TextWatcher{
    private lateinit var binding: ActivityAddBrandBinding
    private lateinit var viewModel: AddBrandViewModel
    private var brandName: String? = null
    private var etbrandName: AppCompatEditText? = null
    private var brandImage: String? = null
    private var tvbrandImage: MaterialTextView? = null
    private val IMAGE_PICK_REQUEST = 124
    var filePath: Uri? = null
    private var photoUpload: DocumentUpload? = null
    private lateinit var brandID: String
    private lateinit var progressBar: ProgressBar
    private lateinit var catId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitleForActivity("Add Brands")
        binding = ActivityAddBrandBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(AddBrandViewModel::class.java)
        etbrandName = binding.etBrandNameAddBrands
        tvbrandImage = binding.tvBrandImageName
        progressBar = binding.pbAddbrand

        val bundle = intent?.extras
        if(bundle!=null){
            catId = bundle.getString(Constants.CatagoryId, "")
        }

        binding.btnSubmitbrandBrandAdd.setOnClickListener {
            ContextExtentions.hideKeyboard(it, this@AddBrandActivity)
            brandName = setErrorMessage(etbrandName, "Enter Valid name")
            brandImage = setErrorMessage(tvbrandImage, "Please select an image")
            if (brandName != "" && brandImage!=null && filePath != null ) {
                //store data in db first then put in realtime db
                progressBar.visibility = View.VISIBLE
                brandID =  viewModel.getNewKey()!!
                photoUpload?.uploadDocumentInFireStore(brandID, filePath.toString(), viewModel.uploadPhoto)
            }
        }

        viewModel.uploadPhoto.observe(this){ path ->
            Log.d(TAG, "onCreate: Photo uploaded ${path}")
            if(path!=""){
                val brand = Brand(brandID , brandName, path)
                viewModel.storebrandDataInFirebase(catId, brand)
            }else{
                progressBar.visibility = View.GONE
            }
        }

        viewModel.brandDataAdded.observe(this){ dbresult ->
            if(dbresult.isSuccess){
                progressBar.visibility = View.GONE
                etbrandName?.setText("")
                tvbrandImage?.setText("")
                Toast.makeText(this@AddBrandActivity, "One brand added", Toast.LENGTH_SHORT).show()
            }else{
                progressBar.visibility = View.GONE
                Toast.makeText(this@AddBrandActivity, "Failed to add brand", Toast.LENGTH_SHORT).show()

            }
        }

        binding.btnAddImage.setOnClickListener {
            tvbrandImage?.setError(null)
            //pick an image
            photoUpload = DocumentUpload(this@AddBrandActivity)
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
                tvbrandImage?.text = imageName
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
            etbrandName?.text.hashCode() -> cleanErrorMessage(etbrandName)
        }
    }

}