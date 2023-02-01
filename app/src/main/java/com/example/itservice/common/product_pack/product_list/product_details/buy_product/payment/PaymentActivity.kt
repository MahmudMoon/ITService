package com.example.itservice.common.product_pack.product_list.product_details.buy_product.payment

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.ViewModelProvider
import com.example.itservice.application.TAG
import com.example.itservice.base.BaseActivity
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.ContextExtentions
import com.example.itservice.databinding.ActivityPaymentBinding
import com.example.itservice.local_db.DatabaseInstance
import com.example.itservice.local_db.DbHelper

class PaymentActivity : BaseActivity(), TextWatcher {
    lateinit var binding: ActivityPaymentBinding
    lateinit var viewModel: PayementViewModel
    lateinit var etCardNumber: AppCompatEditText
    lateinit var etCardOwnerName: AppCompatEditText
    lateinit var etCardExpireDate: AppCompatEditText
    lateinit var etName: AppCompatEditText
    lateinit var etPhoneNumber: AppCompatEditText
    lateinit var etDistrict: AppCompatEditText
    lateinit var etRoadAndHouseNumber: AppCompatEditText
    lateinit var etSubDistrict: AppCompatEditText
    lateinit var confirmPurchase: Button
    lateinit var dbHelper: DbHelper

    var cardNumber: String? = null
    var cardName: String? = null
    var cardExpireDate: String? = null
    var paymentName: String? = null
    var paymentPhone: String? = null
    var paymentRoad: String? = null
    var paymentSubDistrict: String? = null
    var paymentDistrict: String? = null
    var total: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitleForActivity("Payment")
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(PayementViewModel::class.java)
        dbHelper = DatabaseInstance.getDatabaseReference(this)

        etCardNumber = binding.etPaymentCardNumber
        etCardOwnerName = binding.etPaymentCardOwnerName
        etCardExpireDate = binding.etPaymentCardExpireDate

        etName = binding.etPaymentName
        etPhoneNumber = binding.etPaymentPhone
        etRoadAndHouseNumber = binding.etPaymentRoad
        etSubDistrict = binding.etPaymentSubDistrict
        etDistrict = binding.etPaymentDistrict

        confirmPurchase = binding.btnPaymentConfirm

        confirmPurchase.setOnClickListener {
            ContextExtentions.hideKeyboard(it, this@PaymentActivity)

            cardNumber = setErrorMessage(etCardNumber, "enter valid number")
            cardName = setErrorMessage(etCardOwnerName, "enter valid name")
            cardExpireDate = setErrorMessage(etCardExpireDate, "enter valid date")

            paymentName = setErrorMessage(etName, "enter valid name")
            paymentPhone = setErrorMessage(etPhoneNumber, "enter valid number")
            paymentRoad = setErrorMessage(etRoadAndHouseNumber, "enter valid address")
            paymentSubDistrict = setErrorMessage(etSubDistrict, "enter valid subdistrict")
            paymentDistrict = setErrorMessage(etDistrict, "enter valid district")

            if(cardName != "" && cardNumber != "" && cardExpireDate != "" && paymentName != ""
                && paymentPhone != "" && paymentRoad != "" && paymentSubDistrict != "" && paymentDistrict != ""){
                viewModel.confirmPayment(dbHelper)
            }
        }

        viewModel.purchaseableProducts.observe(this){
            if(it.size>0) {
                viewModel.updateProductQuantityInFirebase(it)
            }else{
                displayFailure()
            }
        }

        viewModel.productTableUpdated.observe(this){
            if(it){
                viewModel.getSeletedCarts()
            }else{
                displayFailure()
            }

        }

        viewModel.selectedCarts.observe(this){
            if(it.size>0){
                var idList = ArrayList<String>()
                it.forEach {
                    idList.add(it.id!!)
                }
                if(idList.size>0) {
                    viewModel.deleteSeletedCarts(idList.toTypedArray())
                }else{
                    displayFailure()
                }
            }else{
                displayFailure()
            }
        }

        viewModel.deletedFromLocal.observe(this){
            if(it.size>0){
                Log.d(TAG, "Payment done")
                displaySuccessdialog()
            }else{
                displayFailure()
            }
        }

    }

    override fun onStart() {
        super.onStart()
        val bundle = intent?.extras
        if(bundle!=null){
            total = bundle.getInt(Constants.totalPrice, 0)
        }
    }

    private fun displaySuccessdialog() {
        var alertDialogBuilder = AlertDialog.Builder(this@PaymentActivity)
        alertDialogBuilder.setTitle("Payment Status")
            .setCancelable(false)
            .setMessage("Successfully paid ${total} BTD, for ${paymentName}")
            .setPositiveButton("Okey", object : DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    p0?.dismiss()
                    onBackPressed()
                }
            })
        alertDialogBuilder.show()
    }

    fun displayFailure(){
        Toast.makeText(this@PaymentActivity, "Failed confirming payment", Toast.LENGTH_SHORT).show()

    }

    fun setErrorMessage(view: AppCompatEditText?, message: String): String? {
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
            etName?.text.hashCode() -> cleanErrorMessage(etName)
            etDistrict?.text.hashCode() -> cleanErrorMessage(etDistrict)
            etPhoneNumber?.text.hashCode() -> cleanErrorMessage(etPhoneNumber)
            etRoadAndHouseNumber?.text.hashCode() -> cleanErrorMessage(etRoadAndHouseNumber)
            etCardExpireDate?.text.hashCode() -> cleanErrorMessage(etCardExpireDate)
            etCardOwnerName?.text.hashCode() -> cleanErrorMessage(etCardOwnerName)
            etCardNumber?.text.hashCode() -> cleanErrorMessage(etCardNumber)
            etSubDistrict?.text.hashCode() -> cleanErrorMessage(etSubDistrict)
        }
    }
}