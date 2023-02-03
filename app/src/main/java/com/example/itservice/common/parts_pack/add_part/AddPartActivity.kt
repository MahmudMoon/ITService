package com.example.itservice.common.parts_pack.add_part

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.ViewModelProvider
import com.example.itservice.R
import com.example.itservice.base.BaseActivity
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.common.models.Parts
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.ContextExtentions
import com.example.itservice.databinding.ActivityAddPartBinding

class AddPartActivity : BaseActivity(), TextWatcher {
    private lateinit var binding: ActivityAddPartBinding
    private lateinit var viewModel: AddPartViewModel
    private lateinit var etPartName: AppCompatEditText
    private lateinit var etPartQuantity: AppCompatEditText
    private lateinit var etPartPrice: AppCompatEditText
    private lateinit var btnAddPart: Button
    private var partName: String? = null
    private var partPrice: String? = null
    private var partQuantity: String? = null
    private lateinit var progressBar: ProgressBar
    private val partID: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitleForActivity("Add parts")
        etPartName = binding.etPartNameAddParts
        etPartPrice = binding.etPartPriceAddParts
        etPartQuantity = binding.etPartQuantityPartAdd
        btnAddPart = binding.btnAddPartAddPartsActivity
        progressBar = binding.pbAddParts

        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(AddPartViewModel::class.java)

        var partID = intent?.extras?.getString(Constants.partID, "")
        if(partID!=null && partID != ""){
            viewModel.getPartForId(partID)
        }

        btnAddPart.setOnClickListener {
            ContextExtentions.hideKeyboard(it, this@AddPartActivity)
            partName = setErrorMessage(etPartName, "Enter valid name")
            partPrice = setErrorMessage(etPartPrice, "Enter valid price")
            partQuantity = setErrorMessage(etPartQuantity, "Enter valid quantity")

            if (partName != "" && partQuantity!= "" && partPrice != "" ) {
                //store data in db first then put in realtime db
                progressBar.visibility = View.VISIBLE
                val part = Parts(partID = partID, partName = partName, partPrice = partPrice?.toInt(), partQuantity = partQuantity?.toInt(), partAvailbleAfterRequest = 0)
                viewModel.addNewPart(part)
            }
        }

        viewModel.partAdded.observe(this){
            if(it.isSuccess){
                resetUi()
                Toast.makeText(this, "One part added", Toast.LENGTH_SHORT).show()
            }else{
                resetUi()
                Toast.makeText(this, "Failed to add part", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.partData.observe(this){part ->
            if(part!=null){
                partID = part.partID
                etPartName.setText(part.partName)
                etPartPrice.setText((part.partPrice!!).toString())
                etPartQuantity.setText((part.partQuantity!!).toString())
            }
        }

    }

    fun resetUi(){
        progressBar.visibility = View.GONE
        clearView(etPartName)
        clearView(etPartPrice)
        clearView(etPartQuantity)
    }

    fun clearView(view: AppCompatEditText){
        view.setText("")
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
            etPartName?.text.hashCode() -> cleanErrorMessage(etPartName)
            etPartPrice?.text.hashCode() -> cleanErrorMessage(etPartPrice)
            etPartQuantity?.text.hashCode() -> cleanErrorMessage(etPartQuantity)
        }
    }
}