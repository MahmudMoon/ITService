package com.example.itservice.common.service_pack.add_catagories.add_brand.add_service

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.ViewModelProvider
import com.example.itservice.application.TAG
import com.example.itservice.base.BaseActivity
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.common.models.Brand
import com.example.itservice.common.models.Service
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.ContextExtentions
import com.example.itservice.common.utils.PhotoUpload
import com.example.itservice.databinding.ActivityAddSericeBinding
import com.google.android.material.textview.MaterialTextView

class AddServiceActivity : BaseActivity(), TextWatcher {
    private lateinit var binding: ActivityAddSericeBinding
    private lateinit var viewModel: AddServiceViewModel
    private lateinit var etServiceName: AppCompatEditText
    private lateinit var etServiceCatagory: AppCompatEditText
    private lateinit var etServicePrice: AppCompatEditText
    private lateinit var etBrandName: AppCompatEditText
    private lateinit var etModelName: AppCompatEditText
    private lateinit var tvImageName: TextView
    private lateinit var btnAddImage: Button
    private lateinit var btnAddService: Button
    private lateinit var catId: String
    private lateinit var catName: String
    private lateinit var brandId: String
    private lateinit var brandName: String
    private var serviceName: String? = null
    private var serviceModel: String? = null
    private var servicePrice: String? = null
    private var serviceImage: String? = null
    private var tvserviceImage: MaterialTextView? = null
    private val IMAGE_PICK_REQUEST = 126
    var filePath: Uri? = null
    private var photoUpload: PhotoUpload? = null
    private lateinit var serviceID: String
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitleForActivity("Add Services")
        binding = ActivityAddSericeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(AddServiceViewModel::class.java)

        etServiceName = binding.etServiceNameAddServices
        etServiceCatagory = binding.etServiceCatagoryAddServices
        etServicePrice = binding.etServicePriceAddServices
        etBrandName = binding.etServiceBrandNameAdd
        etModelName = binding.etServiceModelServiceAdd
        tvImageName = binding.tvServiceImageName
        btnAddImage = binding.btnAddImage
        btnAddService = binding.btnSubmitserviceServiceAdd
        progressBar = binding.pbAddService
        tvserviceImage = binding.tvServiceImageName

        disableEditText(etServiceCatagory)
        disableEditText(etBrandName)

        //cat id, cat name, brand id, brand name
        val bundle = intent?.extras
        if(bundle!=null){
            catId = bundle.getString(Constants.CatagoryId,"")
            catName = bundle.getString(Constants.CatagoryName,"")
            brandId = bundle.getString(Constants.BrandId,"")
            brandName = bundle.getString(Constants.BrandName,"")

            etServiceCatagory.setText(catName)
            etBrandName.setText(brandName)
        }

        binding.btnSubmitserviceServiceAdd.setOnClickListener {
            ContextExtentions.hideKeyboard(it, this@AddServiceActivity)
            serviceName = setErrorMessage(etServiceName, "Enter Valid name")
            servicePrice = setErrorMessage(etServicePrice, "Enter Valid Price")
            serviceModel = setErrorMessage(etModelName, "Enter Valid Model")
            serviceImage = setErrorMessage(tvserviceImage, "Please select an image")
            if (serviceName != "" && serviceImage!=null && filePath != null
                && servicePrice != null && serviceModel !=null ) {
                //store data in db first then put in realtime db
                progressBar.visibility = View.VISIBLE
                serviceID =  viewModel.getNewKey()!!
                photoUpload?.uploadImageInFireStore(serviceID, filePath.toString(), viewModel.uploadPhoto)
            }
        }

        viewModel.uploadPhoto.observe(this){ path ->
            Log.d(TAG, "onCreate: Photo uploaded ${path}")
            if(path!=""){
                val service = Service(serviceID ,serviceName , brandName, serviceModel, catName, servicePrice, path)
                viewModel.storeserviceDataInFirebase(catId, brandId, service)
            }else{
                progressBar.visibility = View.GONE
            }
        }

        viewModel.serviceDataAdded.observe(this){ dbresult ->
            if(dbresult.isSuccess){
                progressBar.visibility = View.GONE
                etServiceName?.setText("")
                tvserviceImage?.setText("")
                etServicePrice?.setText("")
                etModelName?.setText("")

                Toast.makeText(this@AddServiceActivity, "One service added", Toast.LENGTH_SHORT).show()
            }else{
                progressBar.visibility = View.GONE
                Toast.makeText(this@AddServiceActivity, "Failed to add service", Toast.LENGTH_SHORT).show()

            }
        }

        binding.btnAddImage.setOnClickListener {
            tvserviceImage?.setError(null)
            //pick an image
            photoUpload = PhotoUpload(this@AddServiceActivity)
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
                tvserviceImage?.text = imageName
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
            etServiceName?.text.hashCode() -> cleanErrorMessage(etServiceName)
            etModelName?.text.hashCode() -> cleanErrorMessage(etModelName)
            etServicePrice?.text.hashCode() -> cleanErrorMessage(etServicePrice)
        }
    }


    fun disableEditText(view: AppCompatEditText){
        view.isEnabled = false
        view.inputType = InputType.TYPE_NULL
    }
}