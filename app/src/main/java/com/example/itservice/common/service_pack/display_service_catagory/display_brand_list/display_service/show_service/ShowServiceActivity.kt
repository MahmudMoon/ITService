package com.example.itservice.common.service_pack.display_service_catagory.display_brand_list.display_service.show_service

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.itservice.R
import com.example.itservice.application.TAG
import com.example.itservice.base.BaseActivity
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.common.models.ServicesTaken
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.ContextExtentions
import com.example.itservice.common.utils.DbInstance
import com.example.itservice.databinding.ActivityDisplayServiceListBinding
import com.example.itservice.databinding.ActivityShowServiceBinding
import com.google.firebase.database.collection.LLRBNode

class ShowServiceActivity : BaseActivity(), TextWatcher {
    private lateinit var binding: ActivityShowServiceBinding
    private lateinit var viewModel: ShowServiceViewModel
    private lateinit var catID: String
    private lateinit var brandID: String
    private lateinit var serviceID: String
    private lateinit var etServiceName: AppCompatEditText
    private lateinit var etServiceCatagory: AppCompatEditText
    private lateinit var etServicePrice: AppCompatEditText
    private lateinit var etBrandName: AppCompatEditText
    private lateinit var etModelName: AppCompatEditText
    private lateinit var etProblemStatement: AppCompatEditText
    private lateinit var ivServiceImage: ImageView
    private lateinit var btnService: Button
    private lateinit var progressBar: ProgressBar
    private var takenService: ServicesTaken = ServicesTaken()
    private var problemStatment: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitleForActivity("Details")
        binding = ActivityShowServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(ShowServiceViewModel::class.java)
        val bundle = intent?.extras
        if(bundle!=null){
           catID = bundle.getString(Constants.CatagoryId, "")
            brandID = bundle.getString(Constants.BrandId, "")
            serviceID = bundle.getString(Constants.ServiceId, "")
            viewModel.getSingleService(catID, brandID, serviceID)
        }

        etServiceName = binding.etServiceNameShowService
        etServiceCatagory = binding.etServiceCatagoryShowService
        etServicePrice = binding.etServicePriceShowService
        etBrandName = binding.etServiceBrandNameShow
        etModelName = binding.etServiceModelServiceShow
        etProblemStatement = binding.etServiceProblemStatement
        ivServiceImage = binding.ivShowServiceImage
        btnService = binding.btnTakeserviceServiceShow
        progressBar = binding.pbShowService
        
        disableEditText(etServiceName)
        disableEditText(etServiceCatagory)
        disableEditText(etServicePrice)
        disableEditText(etBrandName)
        disableEditText(etModelName)

        viewModel.serviceData.observe(this){service ->
            Log.d(TAG, "onCreate: ${service.serviceName}")
            etServiceName.setText(service.serviceName)
            etBrandName.setText(service.brandName)
            etServiceCatagory.setText(service.catagory)
            etServicePrice.setText(service.serviceCost)
            etModelName.setText(service.modelName)
            etProblemStatement.setText("")
            ivServiceImage.load(service.serviceImage)
        }

        btnService.setOnClickListener {
            ContextExtentions.hideKeyboard(it, this@ShowServiceActivity)
            progressBar.visibility = View.VISIBLE
            problemStatment = setErrorMessage(etServiceName, "Enter Valid problem")
            if(problemStatment!="") {
                takenService.status = Constants.ServiceStatus.Pending.name
                takenService.brandID = brandID
                takenService.serviceID = serviceID
                takenService.catagoryID = catID
                takenService.serviceCost = etServicePrice.text.toString()
                takenService.problemStatement = etProblemStatement.text.toString()
                takenService.modelName = etModelName.text.toString()
                val uid = DbInstance.getUserUid(this@ShowServiceActivity)
                takenService.createdByID = uid
                viewModel.getUserNameFromUID(uid)
            }else{
                progressBar.visibility = View.GONE
            }
        }

        viewModel.dataAdded.observe(this){result ->
            if(result.isSuccess){
                progressBar.visibility = View.GONE
                Toast.makeText(this@ShowServiceActivity, "One request added", Toast.LENGTH_SHORT).show()
            }else{
                progressBar.visibility = View.GONE
                Toast.makeText(this@ShowServiceActivity, "Failed to add request", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.userName.observe(this){ name ->
            if(name!="") {
                takenService.createdByName = name
                viewModel.getCatagoryName(catID)
            }else{
                progressBar.visibility = View.GONE
            }
        }

        viewModel.catName.observe(this){catName ->
            if(catName!=""){
                takenService.catagoryName = catName
                viewModel.getBrandName(catID, brandID)
            }else{
                progressBar.visibility = View.GONE
            }
        }

        viewModel.brandName.observe(this){brandName ->
            if(brandName!=null) {
                takenService.brandName = brandName
                viewModel.addNewServiceTakenRequest(takenService)
            }else{
                progressBar.visibility = View.GONE
            }
        }

    }

    fun disableEditText(view: AppCompatEditText){
        view.isEnabled = false
        view.inputType = InputType.TYPE_NULL
        view.setTextColor(Color.BLACK)
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
            etProblemStatement?.text.hashCode() -> cleanErrorMessage(etProblemStatement)
        }
    }
}