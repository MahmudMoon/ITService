package com.example.itservice.cv_carrier

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.ViewModelProvider
import com.example.itservice.R
import com.example.itservice.application.TAG
import com.example.itservice.base.BaseFragment
import com.example.itservice.common.LoginActivity
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.ContextExtentions
import com.example.itservice.common.utils.DbInstance
import com.example.itservice.common.utils.DocumentUpload
import com.example.itservice.databinding.FragmentCVCarrierBinding

interface engineerRegistrationCVPicked{
    fun onCVPicked(filePath: Uri?, imageName: String?)
}

class CVCarrierFragment : BaseFragment(), TextWatcher, engineerRegistrationCVPicked {
    private lateinit var binding: FragmentCVCarrierBinding
    private lateinit var viewModel: CVCarrierViewModel
    private lateinit var tvSingIn: TextView
    private lateinit var btnSignUp: Button
    private var etengineerName: AppCompatEditText? = null
    private var etengineerEmail: AppCompatEditText? = null
    private var etengineerPassword: AppCompatEditText? = null
    private var etengineerCompanyName: AppCompatEditText? = null
    private var etengineerEmployeeID: AppCompatEditText? = null
    private var etengineerNID: AppCompatEditText? = null
    private var etengineerCatagory: AppCompatEditText? = null
    private var etengineerContactNumber: AppCompatEditText? = null
    private var engineerName: String? = null
    private var engineerEmail: String? = null
    private var engineerPassword: String? = null
    private var engineerCompanyName: String? = null
    private var engineerEmployeeID: String? = null
    private var engineerContactNumber: String? = null
    private var engineerCatagory: String? = null
    private var engineerNID: String? = null
    private var progressBar: ProgressBar? = null
    private lateinit var btnSelectCV: Button
    private var tvCVName: TextView? = null
    private var cvUpload: DocumentUpload? = null
    private var cvName: String? =null
    var filePath: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCVCarrierBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as LoginActivity).setTitle("CV Carrier")
        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(CVCarrierViewModel::class.java)
        tvSingIn = binding.tvSignUpEngineerRegistation
        btnSignUp = binding.btnSingUpengineerRegistation
        etengineerName = binding.etFullnameEngineerRegistation
        etengineerEmail = binding.etEmailEngineerRegistation
        etengineerPassword = binding.etPasswordEngineerRegistation
        etengineerCompanyName = binding.etCompanyNameEngineerRegistation
        etengineerEmployeeID = binding.etEmployeeIdEngineerRegistation
        etengineerContactNumber = binding.etContactNumberEngineerRegistation
        etengineerNID = binding.etNidEngineerRegistation
        progressBar = binding.pbEngineerRegistration
        etengineerCatagory = binding.etCatagoryEngineerRegistation
        btnSelectCV = binding.btnSelectCv
        tvCVName = binding.tvSelectCv
        tvCVName?.keyListener = null

        btnSelectCV.setOnClickListener {
            // pick an image
            tvCVName?.setError(null)
            //pick an image
            cvUpload = DocumentUpload(this@CVCarrierFragment.requireActivity() as LoginActivity)
            cvUpload?.selectPdf(Constants.CV_PICK_REQUEST)
        }

        btnSignUp.setOnClickListener {
            ContextExtentions.hideKeyboard(it, requireContext())
            engineerName = setErrorMessage(etengineerName, "Enter Valid Name")
            engineerEmail = setErrorMessage(etengineerEmail, "Enter Valid email")
            engineerPassword = setErrorMessage(etengineerPassword, "Valid Password must be more than 7 characters")
            engineerCompanyName = setErrorMessage(etengineerCompanyName, "Enter valid company Name")
            engineerEmployeeID = setErrorMessage(etengineerEmployeeID, "Enter valid employee id")
            engineerContactNumber = setErrorMessage(etengineerContactNumber, "Enter valid contact number")
            engineerNID = setErrorMessage(etengineerNID, "Enter valid NID")
            engineerCatagory = setErrorMessage(etengineerCatagory, "Enter valid Catagory")
            cvName = setErrorMessage(tvCVName, "Please select a CV")
            if (engineerName != "" && engineerEmail != ""
                && engineerPassword != "" && engineerCompanyName != "" && engineerEmployeeID != ""
                && engineerContactNumber != "" && engineerNID != "" && filePath != null
            ) {
                Log.d(TAG, "onViewCreated: READY for registation")
                progressBar?.visibility = View.VISIBLE
                viewModel.applyEngineerWithEmailPassword(engineerName!!, engineerEmail!!,
                    engineerPassword!! , engineerContactNumber!! , engineerCompanyName!!, engineerEmployeeID!!, engineerNID!!, engineerCatagory!! )

            }
        }

        viewModel.engineerApplyDataSave.observe(viewLifecycleOwner){
            if(it.isSuccess){
                val uid = it.resultData as String
                DbInstance.setUserUid(requireContext(), uid)
                cvUpload?.uploadDocumentInFireStore(uid, filePath.toString(), viewModel.uploadCV)
            }else{
                Toast.makeText(requireContext(), "Application failed", Toast.LENGTH_SHORT).show()
                progressBar?.visibility = View.GONE
            }
        }

        viewModel.uploadCV.observe(viewLifecycleOwner){
            val path = it
            val userUid = DbInstance.getUserUid(requireContext())
            viewModel.updateCvLink(userUid ,path)
            Log.d(TAG, "onViewCreated: USER CV AT "+ path)
        }

        viewModel.isCVUpdated.observe(viewLifecycleOwner){
            progressBar?.visibility = View.GONE
            if(it){
                progressBar?.visibility = View.GONE
                Toast.makeText(requireContext(), "Application successful", Toast.LENGTH_SHORT).show()
                clearUI()
                requireActivity().startActivity(
                    Intent(requireContext(), LoginActivity::class.java)
                    .putExtra(Constants.email, engineerEmail)
                    .putExtra(Constants.password, engineerPassword)
                    .putExtra(Constants.tabSelection, 1))
                moveWithAnimationToAnotherActivity()
            }else{
                Toast.makeText(requireContext(), "Application failed", Toast.LENGTH_SHORT).show()
            }
        }

        tvSingIn.setOnClickListener {
            try {
                (requireActivity() as LoginActivity).navController.navigate(R.id.action_engineerRegistationFragment_to_engineerLoginFragment)
            } catch (e: Exception) {
                Log.e(TAG, "onViewCreated: ${e.localizedMessage}")
            }
        }

    }

    private fun clearUI() {
        etengineerCatagory?.setText("")
        etengineerEmail?.setText("")
        etengineerNID?.setText("")
        etengineerName?.setText("")
        etengineerPassword?.setText("")
        etengineerEmployeeID?.setText("")
        etengineerCompanyName?.setText("")
        etengineerContactNumber?.setText("")
        tvCVName?.setText("")
    }

    fun setErrorMessage(view: TextView?, message: String): String? {
        try {
            val data = view?.text.toString()
            if (data == "" ) {
                view?.error = message
                return data
            }else if(message == "Valid Password must be more than 7 characters" && data.length<=7){
                view?.error = message
                return ""
            }
            return data
        } catch (exception: Exception) {
            view?.error = message
        }
        return null
    }

    fun cleanErrorMessage(view: TextView?) {
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
            etengineerName?.text.hashCode() -> cleanErrorMessage(etengineerName)
            etengineerEmail?.text.hashCode() -> cleanErrorMessage(etengineerEmail)
            etengineerPassword?.text.hashCode() -> cleanErrorMessage(etengineerPassword)
            etengineerCompanyName?.text.hashCode() -> cleanErrorMessage(etengineerCompanyName)
            etengineerEmployeeID?.text.hashCode() -> cleanErrorMessage(etengineerEmployeeID)
            etengineerContactNumber?.text.hashCode() -> cleanErrorMessage(etengineerContactNumber)
            etengineerNID?.text.hashCode() -> cleanErrorMessage(etengineerNID)
            tvCVName?.text.hashCode() -> cleanErrorMessage(tvCVName)
        }
    }
    override fun onCVPicked(filePath: Uri?, imageName: String?) {
        Log.d(TAG, "onImagePicked: u_r "+ filePath + "\n" + imageName)
        if(filePath!=null && imageName != null){
            this.filePath = filePath
            this.cvName = imageName
            tvCVName?.text = imageName
        }
    }
}