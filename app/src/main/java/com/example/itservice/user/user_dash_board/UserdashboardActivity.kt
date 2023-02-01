package com.example.itservice.user.user_dash_board

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itservice.R
import com.example.itservice.application.TAG
import com.example.itservice.base.BaseActivity
import com.example.itservice.common.LoginActivity
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.common.models.Offers
import com.example.itservice.common.service_pack.display_service_catagory.DisplayServiceCatagoryActivity
import com.example.itservice.common.taken_service_catagory.TakenServiceCatagoryActivity
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.example.itservice.common.utils.itemDecorator
import com.example.itservice.databinding.ActivityUserdashboardBinding
import com.example.itservice.local_db.DatabaseInstance
import com.example.itservice.local_db.DbHelper
import com.example.itservice.user.product_catagory.BuyOurProductsCatagoryDisplayActivity
import com.example.itservice.user.product_catagory.product_list.product_details.ProductDetailActivity
import okhttp3.internal.userAgent


interface  OfferItemSelected{
    fun onOfferItemSelected(offer: Offers)
}

class UserdashboardActivity : BaseActivity(), OfferItemSelected {
    private lateinit var binding: ActivityUserdashboardBinding
    private lateinit var viewModel: UserdashboardViewModel
    private lateinit var llyBuyOurProducts: LinearLayout
    private lateinit var llyTakeOurService: LinearLayout
    private lateinit var llyAskForService: LinearLayout
    private lateinit var rvOffers: RecyclerView
    private lateinit var dbHelper: DbHelper
    private lateinit var offerAdapter: OfferAdapter
    private lateinit var tvUserName: TextView
    private lateinit var listOfOffers: ArrayList<Offers>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Home"
        binding = ActivityUserdashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbHelper = DatabaseInstance.getDatabaseReference(this)
        listOfOffers = ArrayList()
        offerAdapter = OfferAdapter(this, listOfOffers)
        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(UserdashboardViewModel::class.java)
        DbInstance.setLastLoginAs(this@UserdashboardActivity, Constants.user)

        llyBuyOurProducts = binding.llyBuyOutProducts
        llyTakeOurService = binding.llyTakeOutService
        llyAskForService = binding.llyAskService
        tvUserName = binding.tvUserName
        rvOffers = binding.rvOffers
        rvOffers.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvOffers.adapter = offerAdapter
        rvOffers.addItemDecoration(itemDecorator(20))

        viewModel.getAllOffers()
        viewModel.offerLiveData.observe(this){
            if(it.size>0) {
                listOfOffers.clear()
                listOfOffers.addAll(it)
                offerAdapter.notifyDataSetChanged()
            }
        }


        llyBuyOurProducts.setOnClickListener {
            val intent = Intent(this@UserdashboardActivity, BuyOurProductsCatagoryDisplayActivity::class.java)
            startActivity(intent)
            moveWithAnimationToAnotherActivity()
        }

        llyTakeOurService.setOnClickListener {
            startActivity(Intent(this@UserdashboardActivity, TakenServiceCatagoryActivity::class.java))
            moveWithAnimationToAnotherActivity()
        }

        llyAskForService.setOnClickListener {
            startActivity(Intent(this@UserdashboardActivity, DisplayServiceCatagoryActivity::class.java))
            moveWithAnimationToAnotherActivity()
        }
    }

    override fun onStart() {
        super.onStart()
        val name = DbInstance.getUserName(this@UserdashboardActivity)
        Log.d(TAG, "onStart: Name "+ name)
        tvUserName.setText(name)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.dash_board_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_item_logout){
            viewModel.logoutuser()
            //clear db also
            viewModel.clearDb(dbHelper)
            finish()
            startActivity(Intent(this@UserdashboardActivity, LoginActivity::class.java))
            moveWithAnimationToAnotherActivity()
        }
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    override fun onOfferItemSelected(offer: Offers) {
        Toast.makeText(this, "Offer selected", Toast.LENGTH_SHORT).show()
        val catId = offer.catagoryId
        val productId = offer.productID
        Log.d(TAG, "onOfferItemSelected: CAT: "+ catId + " PID"+ productId )
        if(catId!=null && productId !=null) {
            val intent = Intent(this@UserdashboardActivity, ProductDetailActivity::class.java)
            intent.putExtra(Constants.CatagoryId, catId)
            intent.putExtra(Constants.productID, productId)
            startActivity(intent)
            moveWithAnimationToAnotherActivity()
        }
    }

}
