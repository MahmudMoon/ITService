package com.example.itservice.admin.show_applications.display_form

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.example.itservice.BuildConfig
import com.example.itservice.application.TAG
import com.example.itservice.base.BaseActivity
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.common.models.Engineer
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.example.itservice.common.utils.DocumentUpload
import com.example.itservice.databinding.ActivityDiplayFormBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.*


class DiplayFormActivity : BaseActivity() {
    private lateinit var binding: ActivityDiplayFormBinding
    private lateinit var viewModel: DisplayFormViewModel
    private var etengineerName: AppCompatEditText? = null
    private var etengineerEmail: AppCompatEditText? = null
    private var etengineerPassword: AppCompatEditText? = null
    private var etengineerCompanyName: AppCompatEditText? = null
    private var etengineerEmployeeID: AppCompatEditText? = null
    private var etengineerNID: AppCompatEditText? = null
    private var etengineerCatagory: AppCompatEditText? = null
    private var etengineerContactNumber: AppCompatEditText? = null
    private var tvCvName: TextView? = null
    private val NOCVFOUND = "No CV Found"
    private var btnDownLoadCV: Button? = null
    private var cvLink: String? = null
    private lateinit var documentUpload: DocumentUpload
    private var dialog : AlertDialog? = null
    private var btnAccept: Button? = null
    private var btnReject: Button? = null
    private var engineer: Engineer? = null
    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiplayFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitleForActivity("Application form")
        etengineerName = binding.etFullnameEngineerForm
        etengineerEmail = binding.etEmailEngineerForm
        etengineerPassword = binding.etPasswordEngineerForm
        etengineerCompanyName = binding.etCompanyNameEngineerForm
        etengineerEmployeeID = binding.etEmployeeIdEngineerForm
        etengineerContactNumber = binding.etContactNumberEngineerForm
        etengineerNID = binding.etNidEngineerForm
        etengineerCatagory = binding.etCatagoryEngineerForm
        tvCvName = binding.tvDownloadCvName
        btnDownLoadCV = binding.btnDownloadCv
        btnAccept = binding.btnAcceptForm
        btnReject = binding.btnRejectForm
        progressBar = binding.pbEngineerAccpet

        etengineerName?.keyListener = null
        etengineerEmail?.keyListener = null
        etengineerPassword?.keyListener = null
        etengineerCatagory?.keyListener = null
        etengineerContactNumber?.keyListener = null
        etengineerNID?.keyListener = null
        etengineerEmployeeID?.keyListener = null
        etengineerCompanyName?.keyListener = null


        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(DisplayFormViewModel::class.java)
        documentUpload = DocumentUpload(this)

        getUidForApplication().let {
            if(it!=null){
                viewModel.getFormFor(it)
            }
        }


        viewModel.formData.observe(this){
            if(it!=null){
                this.engineer = it
                Log.d(TAG, "onCreate: name: "+it.fullName)
                etengineerName?.setText(it.fullName)
                etengineerEmail?.setText(it.email)
                etengineerCatagory?.setText(it.catagory)
                etengineerPassword?.setText(it.password)
                etengineerNID?.setText(it.nid)
                etengineerContactNumber?.setText(it.contactNumber)
                etengineerCompanyName?.setText(it.companyName)
                etengineerEmployeeID?.setText(it.employeeID)
                if(it.cvLink!=null &&  it.cvLink.length>0){
                    tvCvName?.setText("CV_for_${it.fullName}.pdf")
                    cvLink = it.cvLink
                    btnDownLoadCV?.isEnabled = true
                }else{
                    cvLink = null
                    tvCvName?.setText(NOCVFOUND)
                    btnDownLoadCV?.isEnabled = false
                }
            }else{
                Toast.makeText(this@DiplayFormActivity, "no form found", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.downloadCompleted.observe(this){
            if(it.isSuccess){
                Log.d(TAG, "onCreate: pathL "+ it.resultData)
                if(it.resultData!=null){
                    if(it.resultData is String){
                        val path = it.resultData
                        try {
                            val ref = this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                            val file = File(ref, getFileName())
                            val exists = file.exists()
                            Log.d(TAG, "onCreate: exist " + exists)
                            // Get URI and MIME type of file

                            // Get URI and MIME type of file
                            val uri = FileProvider.getUriForFile(
                                Objects.requireNonNull(getApplicationContext()),
                                BuildConfig.APPLICATION_ID + ".provider", file);

                            val mime = contentResolver.getType(uri)
                            val intent = Intent()
                            intent.action = Intent.ACTION_VIEW
                            intent.setDataAndType(uri, mime)
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            startActivity(intent)

                        }catch (e: Exception){
                            Log.d(TAG, "onCreate: errror "+e.message)
                            Toast.makeText(this@DiplayFormActivity, "Failed to open file", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Log.d(TAG, "onCreate: failed  ")
                        Toast.makeText(this@DiplayFormActivity, "Failed to download CV", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Log.d(TAG, "onCreate: failed  ")
                    Toast.makeText(this@DiplayFormActivity, "Failed to download CV", Toast.LENGTH_SHORT).show()
                }
            }else{
                Log.d(TAG, "onCreate: failed  ")
                Toast.makeText(this@DiplayFormActivity, "Failed to download CV", Toast.LENGTH_SHORT).show()
            }
        }

        btnDownLoadCV?.setOnClickListener {
            if(cvLink!=null){
                getAlertBuilder()?.show()
                CoroutineScope(Dispatchers.IO).launch {
                    val tmpName = getFileName()
                    documentUpload.downloadFile(this@DiplayFormActivity, cvLink!!,tmpName, viewModel.downloadCompleted)
                }.invokeOnCompletion {
                    Log.d(TAG, "onCreate: done download")
                    getAlertBuilder()?.dismiss()
                }
            }
        }

        btnAccept?.setOnClickListener {
            val email = engineer?.email
            val password = engineer?.password
            if(email != null && password != null && email != "" && password != ""){
                progressBar?.visibility = View.VISIBLE
                Log.d(TAG, "onCreate: admin before uid: "+DbInstance.getAuthInstance().uid)
                Log.d(TAG, "onCreate: email: "+email + " paas "+ password )
                viewModel.registerEngineer(email, password)
            }else{
                viewModel.getFormFor(getUidForApplication())
            }
        }


        viewModel.adminRelogined.observe(this){
            if(it){
                //delete application form and back
                getUidForApplication().let { applicationID ->
                    if(applicationID!=null)  viewModel.deleteApplication(applicationID)
                    else{
                        progressBar?.visibility = View.GONE
                        Log.d(TAG, "onCreate: did not get application table uid ")
                        displayMessage("Failed to accept")
                    }
                }

            }else{
                progressBar?.visibility = View.GONE
                Log.d(TAG, "onCreate: failed to admin rejoin " )
                displayMessage("Failed to accept")
            }
        }

        viewModel.engineerDbUpdated.observe(this){
            if(it){
                // admin relogin
                DbInstance.getAuthInstance().signOut()
                val adminEmail = DbInstance.getAdminLastEmail(this@DiplayFormActivity)
                val adminPassword = DbInstance.getAdminLastPassword(this@DiplayFormActivity)
                viewModel.registerAdmin(adminEmail, adminPassword)
                Log.d(TAG, "onCreate: admin after relogin: "+DbInstance.getAuthInstance().uid)
            }else{
                progressBar?.visibility = View.GONE
                Log.d(TAG, "onCreate: failed to update enginner db")
                displayMessage("Failed to accept")
            }
        }



        viewModel.isRegistered.observe(this){
            if(it.isSuccess){
                //update db for enginner
                if(engineer!=null){
                    engineer?.uid = it.resultData as String
                    viewModel.updateEngineerTable(engineer!!)
                }else{
                    progressBar?.visibility = View.GONE
                    Log.d(TAG, "onCreate: failed to fetch enginner " + it.resultData)
                    displayMessage("Failed to accept")
                }
            }else{
                progressBar?.visibility = View.GONE
                Log.d(TAG, "onCreate: failed to register " + it.resultData)
                displayMessage("Failed to accept")
            }
        }

        btnReject?.setOnClickListener {
            //delete and back
            getUidForApplication().let {
                if(it!=null) viewModel.deleteApplication(it)
            }
        }

        viewModel.formDeleted.observe(this){
            progressBar?.visibility = View.GONE
            if(it) {
                Log.d(TAG, "onCreate: successfully deleted application fomr")
                displayMessage("successfully done")
            }else{
                Log.d(TAG, "onCreate: successfully deleted application fomr")
                displayMessage("failed")
            }
            onBackPressed()
        }


    }

    private fun displayMessage(s: String) {
        Toast.makeText(this@DiplayFormActivity, s, Toast.LENGTH_SHORT).show()
    }

    private fun getAlertBuilder() : AlertDialog?{
        if(dialog==null) {
            dialog = AlertDialog.Builder(this@DiplayFormActivity)
                .setTitle("CV Download")
                .setMessage("CV download in progress, please wait")
                .setCancelable(false)
                .setView(ProgressBar(this@DiplayFormActivity))
                .create()
        }
        return dialog
    }

    fun getFileName(): String{
        val fileName =  tvCvName?.text.toString()
        val arys = fileName.split(" ")
        var tmpName = ""
        arys.forEach {
            tmpName+=it
        }
        return tmpName
    }

    fun getUidForApplication(): String?{
        if(intent?.extras!=null){
            val uid = intent?.extras!!.getString(Constants.engUid, "")
            return uid
        }

        return null
    }


}