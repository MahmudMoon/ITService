package com.example.itservice.common.taken_service_catagory.service_list.service_modify

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatSpinner
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itservice.R
import com.example.itservice.application.TAG
import com.example.itservice.base.BaseActivity
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.common.models.Notifications
import com.example.itservice.common.models.Parts
import com.example.itservice.common.models.ServicesTaken
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.ContextExtentions
import com.example.itservice.common.utils.DbInstance
import com.example.itservice.databinding.ActivityServiceModifyBinding


class ServiceModifyActivity : BaseActivity(), EngineerSelection, OnItemSelectedListener, PartsSelection, PartQueryItemSelected, TextWatcher {
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
    private lateinit var fragContainerforEngineers: FragmentContainerView
    private lateinit var fragContainerforParts: FragmentContainerView
    private lateinit var ibtnSearchEng: ImageButton
    private lateinit var btnModifyService: Button
    private lateinit var takeServiceId: String
    private lateinit var takenService: ServicesTaken
    private lateinit var statusSpinner: AppCompatSpinner
    val tmpStatusHolder = ArrayList<String>()
    private lateinit var navController_for_eng: NavController
    private lateinit var progressBar: ProgressBar
    private lateinit var rvPartsQuery: RecyclerView
    private lateinit var btnAskForAPart: Button
    private lateinit var navController_for_parts: NavController
    private var partsList = ArrayList<Parts>()
    private lateinit var partsQueryAdapter: PartsQueryAdapter

    private lateinit var llyConfirmationDialog: LinearLayout
    private lateinit var etInputQuantity: EditText
    private lateinit var btnAddConfirm: Button
    private lateinit var btnCancelConfirm: Button

    private var newlyAddedPart: Parts? = null

    private var discardParts = HashMap<String, Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitleForActivity("Modify Service")
        binding = ActivityServiceModifyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(ServiceModifyViewModel::class.java)

        //for engineers
        val navHostFragment_eng =
            supportFragmentManager.findFragmentById(R.id.assignedEngineerSearchFragmentContainer) as NavHostFragment
        navController_for_eng = navHostFragment_eng.navController

        //for parts
        val navHostFragment_parts =
            supportFragmentManager.findFragmentById(R.id.available_parts_containerFragmentContainer) as NavHostFragment
        navController_for_parts = navHostFragment_parts.navController

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
        fragContainerforEngineers = binding.assignedEngineerSearchFragmentContainer
        fragContainerforParts = binding.availablePartsContainerFragmentContainer
        ibtnSearchEng = binding.ibtnSearchEng
        btnModifyService = binding.btnSubmitserviceServiceModify
        statusSpinner = binding.spnSelectStatusServiceModify
        progressBar = binding.pbModifiyTakenService
        rvPartsQuery = binding.rvPartsRequired
        btnAskForAPart = binding.btnPickAPart
        llyConfirmationDialog = binding.llyConfirmationDialog
        etInputQuantity = binding.etUnitQuantity
        btnAddConfirm = binding.btnAddConfirm
        btnCancelConfirm = binding.btnCancelConfirm


        partsQueryAdapter = PartsQueryAdapter(this, partsList)
        rvPartsQuery.layoutManager = LinearLayoutManager(this)
        rvPartsQuery.adapter = partsQueryAdapter


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
       // hideAView(btnModifyService)

        if(DbInstance.getLastLoginAs(this).equals(Constants.user)){
            enableEditText(etServiceProblemStatement)
            hideAView(ibtnSearchEng)
            statusSpinner.isEnabled = false
            hideAView(btnAskForAPart)
        }else if(DbInstance.getLastLoginAs(this).equals(Constants.admin)){
            hideAView(btnAskForAPart)
            binding.tvAcceptIfAdmin.visibility = View.VISIBLE
            binding.tvDeclineIfAdmin.visibility = View.VISIBLE
        }else if(DbInstance.getLastLoginAs(this).equals(Constants.engineer)){
            hideAView(ibtnSearchEng)
        }

        val bundle = intent?.extras
        if(bundle!=null){
            val takeServiceId = bundle.getString(Constants.takenServiceId, "")
            val createrId = bundle.getString(Constants.userID, "")
            viewModel.getTakenServiceObject(createrId,takeServiceId)
        }

        viewModel.takenObject.observe(this){objectValue ->
            if(objectValue !=null) {
                takenService = objectValue
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
                if (statusIndex >= 0)
                    statusSpinner.setSelection(statusIndex)

                val parts = takenService.parts
                if (parts != null) {
                    partsList.clear()
                    partsList.addAll(parts!!)
                    partsQueryAdapter.notifyDataSetChanged()
                }

            }

        }



        ibtnSearchEng.setOnClickListener {
            Log.d(TAG, "onCreate: Clicked")
            if(fragContainerforEngineers.visibility == View.VISIBLE){
                fragContainerforEngineers.visibility = View.GONE
            }else if(fragContainerforEngineers.visibility == View.GONE){
                fragContainerforEngineers.visibility = View.VISIBLE
            }

        }

        btnAskForAPart.setOnClickListener {
            if(fragContainerforParts.visibility == View.VISIBLE){
                fragContainerforParts.visibility = View.GONE
            }else if(fragContainerforParts.visibility == View.GONE){
                fragContainerforParts.visibility = View.VISIBLE
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
                if(discardParts.size>0)
                    viewModel.updatePartData(discardParts)
            }else if(DbInstance.getLastLoginAs(this).equals(Constants.engineer)){
                takenService.parts?.forEach { part ->
                    if(part.partAvailbleAfterRequest!=null) part.partAvailbleAfterRequest = null
                }
                viewModel.updateTakenService( takenService)
            }else{
                progressBar.visibility = View.GONE
            }
        }

        viewModel.dataUpdateCheck.observe(this){
            Log.d(TAG, "onCreate: Data update completed")
            progressBar.visibility = View.GONE
            if(it.isSuccess){
                var notifications = Notifications(notificationId = null, takenServiceId = takenService.id, createdById = takenService.createdByID)
                viewModel.sendNotificationFor(takenService.assignedEngineerID, takenService.createdByID, notifications)
                Toast.makeText(this, "Successfully modified service", Toast.LENGTH_SHORT).show()
                if(DbInstance.getLastLoginAs(this@ServiceModifyActivity).equals(Constants.engineer)) takenService.parts = null
            }else{
                Toast.makeText(this, "Failed to modify service", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.engData.observe(this){ engineer ->
            takenService.assignedEngineerID = engineer.uid
            takenService.assignedEngineerName = engineer.fullName
            etServiceAssignedTo.setText(takenService.assignedEngineerName)
        }


        btnAddConfirm.setOnClickListener {
            val quantity = etInputQuantity.text.toString()
            ContextExtentions.hideKeyboard(it, this)
            if(newlyAddedPart!=null && quantity!= ""){
                var availableQuantity = (newlyAddedPart?.partQuantity)
                val requireQuantity = quantity.toInt()
                if(availableQuantity!= null){
                    if(availableQuantity>=requireQuantity){
                        availableQuantity = availableQuantity - requireQuantity
                        newlyAddedPart?.partQuantity = availableQuantity
                        viewModel.updateValueOfpartsInRootTable(newlyAddedPart!!)
                        newlyAddedPart?.partAvailbleAfterRequest = availableQuantity
                            llyConfirmationDialog.visibility = View.GONE
                            newlyAddedPart?.partQuantity = requireQuantity
                            if(takenService.parts == null){
                                takenService.parts = ArrayList()
                            }
                            takenService.parts?.add(newlyAddedPart!!)
                            takenService.parts?.size
                            partsList.clear()
                            partsList.addAll(takenService.parts!!)
                            partsQueryAdapter.notifyDataSetChanged()
                    }else{
                        Toast.makeText(this, "Quantity not available", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this, "Quantity not available", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Please selct a proper item", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancelConfirm.setOnClickListener {
            llyConfirmationDialog.visibility = View.GONE
        }
    }

    private fun hideAView(view: View) {
        view.visibility = View.GONE
    }

    private fun unHideAView(view: View) {
        if(view.visibility != View.VISIBLE) view.visibility = View.VISIBLE
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
        fragContainerforEngineers.visibility = View.GONE
        viewModel.getEngineerDataFromUid(engineerUid)
    }


    //spinner item selection
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val item: String = p0?.getItemAtPosition(p2).toString()
        Toast.makeText(this, "Selected: $item", Toast.LENGTH_LONG).show()
        try {
            takenService.status = item
        }catch (e: java.lang.Exception){
            Log.e(TAG, "onItemSelected: ${e.localizedMessage}")
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onPartSelected(part: Parts) {
        Toast.makeText(this, "One part added", Toast.LENGTH_SHORT).show()
        newlyAddedPart = part
        llyConfirmationDialog.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(takenService.parts!=null){
            takenService.parts?.forEach { part->
                Log.d(TAG, "onBackPressed: availableQuantity: ${part.partQuantity} --> ${part.partAvailbleAfterRequest}")
                if(part.partQuantity!= null && part.partAvailbleAfterRequest!=null) {
                    part.partQuantity = part.partQuantity!! + part.partAvailbleAfterRequest!!
                    viewModel.updateValueOfpartsInRootTable(part)
                }
            }
        }
    }

    override fun onCrossClickedByAdmin(partID: String, position: Int) {
        val _part = takenService.parts?.get(position)!!
       takenService.parts?.removeAt(position)
        partsList.clear()
        partsList.addAll(takenService.parts!!)
        partsQueryAdapter.notifyDataSetChanged()
        discardParts.put(key = _part.partID!!, _part.partQuantity!!)
    }

    override fun onTickClickedByAdmin(partID: String, position: Int) {
        var _part = takenService.parts?.get(position)!!
        _part.isAccepted = true
        partsList.clear()
        partsList.addAll(takenService.parts!!)
        partsQueryAdapter.notifyDataSetChanged()
        val previousCost = etServicePrice.text.toString().toInt()
        val updatedCost = ((_part.partQuantity!!) * (_part.partPrice!!) + previousCost )
        etServicePrice.setText(updatedCost.toString())
        takenService.serviceCost = updatedCost.toString()
    }


    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun afterTextChanged(p0: Editable?) {
         unHideAView(btnModifyService)
    }
}

interface PartQueryItemSelected{
    fun onCrossClickedByAdmin(partID: String, position: Int)
    fun onTickClickedByAdmin(partID: String, position: Int)
}
