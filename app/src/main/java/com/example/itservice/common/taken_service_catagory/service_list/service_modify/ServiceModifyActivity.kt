package com.example.itservice.common.taken_service_catagory.service_list.service_modify

import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatSpinner
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.itservice.R
import com.example.itservice.application.TAG
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.common.models.ServicesTaken
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.example.itservice.databinding.ActivityServiceModifyBinding


class ServiceModifyActivity : AppCompatActivity(), EngineerSelection, OnItemSelectedListener {
    private lateinit var binding: ActivityServiceModifyBinding
    private lateinit var viewModel: ServiceModifyViewModel
   // private lateinit var etServiceName: AppCompatEditText
    private lateinit var etServiceID: AppCompatEditText
    private lateinit var etServiceStatus: Spinner
    private lateinit var etServiceAssignedTo: AppCompatEditText
    private lateinit var etServiceCreatedBy: AppCompatEditText
    private lateinit var etServiceCatagoryName: AppCompatEditText
    private lateinit var etServiceBrandName: AppCompatEditText
    private lateinit var etServiceModelName: AppCompatEditText
    private lateinit var etServicePrice: AppCompatEditText
    private lateinit var etServiceProblemStatement: AppCompatEditText
    private lateinit var fragContainer: FragmentContainerView
    private lateinit var ibtnSearchEng: ImageButton
    private lateinit var btnModifyService: Button
    private lateinit var takeServiceId: String
    private lateinit var takenService: ServicesTaken
    private lateinit var statusSpinner: AppCompatSpinner
    val tmpStatusHolder = ArrayList<String>()
    lateinit var navController: NavController
    lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceModifyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(ServiceModifyViewModel::class.java)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.assignedEngineerSearchFragmentContainer) as NavHostFragment
        navController = navHostFragment.navController

        // etServiceName = binding.etServiceNameModifyServices
        etServiceID = binding.etServiceIDModifyServices
        etServiceStatus = binding.spnSelectStatusServiceModify

        etServiceAssignedTo = binding.etServiceAssignedToModifyServices
        etServiceCreatedBy = binding.etServiceCreatedByModifyServices
        etServiceCatagoryName = binding.etServiceCatagoryModifyServices
        etServiceBrandName = binding.etServiceBrandNameModify
        etServiceModelName = binding.etServiceModelServiceModify
        etServicePrice = binding.etServicePriceModifyServices
        etServiceProblemStatement = binding.etProblemStatementServiceServiceModify
        fragContainer = binding.assignedEngineerSearchFragmentContainer
        ibtnSearchEng = binding.ibtnSearchEng
        btnModifyService = binding.btnSubmitserviceServiceModify
        statusSpinner = binding.spnSelectStatusServiceModify
        progressBar = binding.pbModifiyTakenService

        val statusData = Constants.ServiceStatus.values()
        statusData.forEach {
            tmpStatusHolder.add(it.name)
        }
        val aryAdapter = ArrayAdapter<String>(this,  android.R.layout.simple_spinner_item, tmpStatusHolder)
        aryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.adapter = aryAdapter
        statusSpinner.setOnItemSelectedListener(this);



        disableEditText(etServiceID)
        disableEditText(etServiceBrandName)
        disableEditText(etServiceModelName)
        disableEditText(etServiceCatagoryName)
        disableEditText(etServiceProblemStatement)
        disableEditText(etServicePrice)
        disableEditText(etServiceCreatedBy)
        disableEditText(etServiceAssignedTo)

        if(DbInstance.getLastLoginAs(this).equals(Constants.user)){
            enableEditText(etServiceProblemStatement)
            hideAView(ibtnSearchEng)
            statusSpinner.isEnabled = false
        }else if(DbInstance.getLastLoginAs(this).equals(Constants.admin)){
            enableEditText(etServiceProblemStatement)
        }else if(DbInstance.getLastLoginAs(this).equals(Constants.engineer)){

        }

        val bundle = intent?.extras
        if(bundle!=null){
            val takeServiceId = bundle.getString(Constants.takenServiceId, "")
            val createrId = bundle.getString(Constants.userID, "")
            viewModel.getTakenServiceObject(createrId,takeServiceId)
        }

        viewModel.takenObject.observe(this){takenObject ->
            takenService = takenObject
            Log.d(TAG, "onCreate: takenObjcet ${takenService.id}")
           // etServiceName.setText(takenObject.serviceName)
            takeServiceId = takenService.id!!
            etServiceID.setText(takenService.id)
            etServiceAssignedTo.setText(takenService.assignedEngineerName)
            etServiceCreatedBy.setText(takenService.createdByName)
            etServiceCatagoryName.setText(takenService.catagoryName)
            etServiceBrandName.setText(takenService.brandName)
            etServiceModelName.setText(takenService.modelName)
            etServicePrice.setText(takenService.serviceCost)
            etServiceProblemStatement.setText(takenService.problemStatement)
            //change status
            val status = takenService.status
            val statusIndex = tmpStatusHolder.indexOf(status)
            if(statusIndex>=0)
                statusSpinner.setSelection(statusIndex)
        }

        ibtnSearchEng.setOnClickListener {
            Log.d(TAG, "onCreate: Clicked")
            if(fragContainer.visibility == View.VISIBLE){
                fragContainer.visibility = View.GONE
            }else if(fragContainer.visibility == View.GONE){
                fragContainer.visibility = View.VISIBLE
            }

        }

        btnModifyService.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            if(DbInstance.getLastLoginAs(this).equals(Constants.user)){
                val problemStatement = etServiceProblemStatement.text.toString()
                takenService.problemStatement = problemStatement
                viewModel.updateTakenService( takenService)
            }else if(DbInstance.getLastLoginAs(this).equals(Constants.admin)){
                // update the taken service for status change
                viewModel.updateTakenService( takenService)
            }else if(DbInstance.getLastLoginAs(this).equals(Constants.engineer)){

            }
        }

        viewModel.dataUpdateCheck.observe(this){
            Log.d(TAG, "onCreate: Data update completed")
            progressBar.visibility = View.GONE
            if(it.isSuccess){
                Toast.makeText(this, "Successfully modified service", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Failed to modify service", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.engData.observe(this){ engineer ->
            takenService.assignedEngineerID = engineer.uid
            takenService.assignedEngineerName = engineer.fullName
            etServiceAssignedTo.setText(takenService.assignedEngineerName)
        }
    }

    private fun hideAView(ibtnSearchEng: ImageButton) {
        ibtnSearchEng.visibility = View.GONE
    }

    fun disableEditText(view: AppCompatEditText){
        view.isEnabled = false
        view.inputType = InputType.TYPE_NULL
        //view.setTextColor(Color.BLACK)
    }

    fun enableEditText(view: AppCompatEditText){
        view.isEnabled = true
        view.inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
        view.setTextColor(Color.BLACK)
    }

    override fun onEngineerSelected(engineerUid: String) {
        Log.d(TAG, "onEngineerSelected: ${engineerUid}")
        fragContainer.visibility = View.GONE
        viewModel.getEngineerDataFromUid(engineerUid)
    }


    //spinner item selection
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val item: String = p0?.getItemAtPosition(p2).toString()
        Toast.makeText(this, "Selected: $item", Toast.LENGTH_LONG).show()
        takenService.status = item
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}