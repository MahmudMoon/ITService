package com.example.itservice.admin.offer_modify

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itservice.application.TAG
import com.example.itservice.base.BaseActivity
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.common.models.Offers
import com.example.itservice.common.utils.ContextExtentions
import com.example.itservice.common.utils.RVItemTouchHelperProvider
import com.example.itservice.common.utils.itemDecorator
import com.example.itservice.databinding.ActivityOfferModifyBinding
import com.example.itservice.user.user_dash_board.OfferAdapter

interface iOfferSelectedForEdit{
    fun onOfferClicked(offers: Offers)
}

class OfferModifyActivity : BaseActivity(), iOfferSelectedForEdit {
    private lateinit var binding: ActivityOfferModifyBinding
    private lateinit var viewModel: OfferModifyViewModel
    private var rvOfferssAdapter: OfferAdapter? = null
    private var offerssList = ArrayList<Offers>()
    private var rvDisplayOfferss: RecyclerView? = null
    private lateinit var etOffersTitle: EditText
    private lateinit var etOffersPrice: EditText
    private lateinit var etOffersDescrition: EditText
    private lateinit var btnAddOffer: Button
    private lateinit var etCurrentPrice: EditText
    private  var offer: Offers? = null
    private lateinit var llyAddOfferContainer: CardView
    private lateinit var ivCross: ImageView
    private lateinit var progrssView: ProgressBar
    private var isDeleted = MutableLiveData<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOfferModifyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitleForActivity("Offer modify")
        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(OfferModifyViewModel::class.java)

        rvOfferssAdapter = OfferAdapter(this@OfferModifyActivity, offerssList)
        rvDisplayOfferss = binding.rvOfferListItems
        etOffersTitle = binding.etOfferTitleOffer
        etOffersPrice = binding.etOfferPriceOffer
        etOffersDescrition = binding.etOfferDescriptionOffer
        etCurrentPrice = binding.etOfferCurrentPriceOffer
        btnAddOffer = binding.btnAddOfferOffer
        llyAddOfferContainer = binding.llyBottomContainerOffer
        ivCross = binding.ivCrossOfferModify
        progrssView = binding.pgBar

        etCurrentPrice.keyListener = null
        etOffersTitle.keyListener = null


        rvDisplayOfferss?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvDisplayOfferss?.adapter = rvOfferssAdapter
        rvDisplayOfferss?.addItemDecoration(itemDecorator(10))
        val touchUpObject = RVItemTouchHelperProvider(isDeleted)
        val touchUpListener = touchUpObject.getItemTouchHelper()
        val itemTouchHelper = ItemTouchHelper(touchUpListener)
        itemTouchHelper.attachToRecyclerView(rvDisplayOfferss)

        viewModel.getAllOffers()
        viewModel.offerContainer.observe(this){
            if(progrssView.visibility == View.VISIBLE){
                progrssView.visibility = View.GONE
            }
            offerssList.clear()
            offerssList.addAll(it)
            rvOfferssAdapter?.notifyDataSetChanged()
        }

        isDeleted.observe(this){
            if(it>=0){
                progrssView.visibility = View.VISIBLE
                Log.d(TAG, "onCreate: "+ "One item deleted at "+it)
                Toast.makeText(this@OfferModifyActivity, "One item deleted at "+it, Toast.LENGTH_SHORT).show()
                this.offer = offerssList.get(it)
                viewModel.removeOneOffer(offerssList.get(it).id!!)
                clearAllSeletedFields()
            }else{
                progrssView.visibility = View.GONE
                Log.d(TAG, "onCreate: "+ "One item deleted at "+it)
                Toast.makeText(this@OfferModifyActivity, "Failed to delete", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isOfferDeleted.observe(this){
            if(!it){
                progrssView.visibility = View.GONE
            }else{
                progrssView.visibility = View.GONE
                if(offer?.productID!=null && offer?.catagoryId != null) {
                    viewModel.changeOfferPriceInProductTable(
                        offer?.productID,
                        offer?.catagoryId,
                        null
                    )
                }
            }
        }

        btnAddOffer.setOnClickListener {
            ContextExtentions.hideKeyboard(it, this@OfferModifyActivity)
            progrssView.visibility = View.VISIBLE
            try {
                val offerName = etOffersTitle.text.toString()
                val offerCurrentPrice = etCurrentPrice.text.toString()
                val offerNewPrice = etOffersPrice.text.toString()
                val offerDescription = etOffersDescrition.text.toString()
                val price = offerNewPrice.toIntOrNull()
                val currentPrice = offerCurrentPrice.toIntOrNull()

                if(offerName.length>0 && offerCurrentPrice.length>0 && offerNewPrice.length>0 && offerDescription.length >0 ) {
                    val newOffer = Offers(
                        id = offer?.id,
                        title = offerName,
                        productID = offer?.productID,
                        imageUrl = offer?.imageUrl,
                        previousPrice = currentPrice,
                        newPrice = price,
                        catagoryId = offer?.catagoryId,
                        details = offerDescription
                    )
                    this.offer = newOffer
                    viewModel.modifyAnOffer(newOffer)
                }else{
                    progrssView.visibility = View.GONE
                    Toast.makeText(this@OfferModifyActivity, "Please provide offer info", Toast.LENGTH_SHORT).show()
                }
            }catch (e: Exception){
                Toast.makeText(this@OfferModifyActivity, "Failed to add offer", Toast.LENGTH_SHORT).show()
                progrssView.visibility = View.GONE
            }
        }

        viewModel.isOfferModified.observe(this){
            progrssView.visibility = View.GONE
            if(it){
                Log.d(TAG, "onCreate: offer new price: observer "+ offer?.newPrice)
                Toast.makeText(this@OfferModifyActivity, "Offer modified", Toast.LENGTH_SHORT).show()
                if(offer?.productID!=null && offer?.catagoryId != null && offer?.newPrice!= null) {
                    viewModel.changeOfferPriceInProductTable(
                        offer?.productID,
                        offer?.catagoryId,
                        offer?.newPrice!!
                    )
                }
            }else{
                Toast.makeText(this@OfferModifyActivity, "Modification failed", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun clearAllSeletedFields() {
        this.offer = null
        etOffersTitle.setText("")
        etOffersPrice.setText("")
        etCurrentPrice.setText("")
        etOffersDescrition.setText("")
    }

    override fun onOfferClicked(offers: Offers) {
        this.offer = offers
        Log.d(TAG, "onOfferClicked: "+offers.id)
        etOffersTitle.setText(offers.title)
        etOffersPrice.setText(offers.newPrice.toString())
        etCurrentPrice.setText(offers.previousPrice.toString())
        etOffersDescrition.setText(offers.details)
    }
}