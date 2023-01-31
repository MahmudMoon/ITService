package com.example.itservice.admin.admin_dashboard

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.itservice.R
import com.example.itservice.admin.addOffers.AddOfferActivity
import com.example.itservice.admin.offer_modify.OfferModifyActivity
import com.example.itservice.admin.users_and_engineers_list.UsersAndEngineersListActivity
import com.example.itservice.base.BaseActivity
import com.example.itservice.common.LoginActivity
import com.example.itservice.common.display_parts.DisplayPartsActivity
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.common.service_pack.display_service_catagory.DisplayServiceCatagoryActivity
import com.example.itservice.common.taken_service_catagory.TakenServiceCatagoryActivity
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.example.itservice.databinding.ActivityAdminDashBoardBinding
import com.example.itservice.user.product_catagory.BuyOurProductsCatagoryDisplayActivity

class AdminDashBoardActivity : BaseActivity() {
    private lateinit var binding: ActivityAdminDashBoardBinding
    private lateinit var viewModel: AdminDashBoradViewModel
    private lateinit var llyAnalysisServiceTakenTasks: LinearLayout
    private lateinit var llyProductsModify: LinearLayout
    private lateinit var llyServiceModify: LinearLayout
    private lateinit var llyOffersModify: LinearLayout
    private lateinit var llyPartsModify: LinearLayout
    private lateinit var llyUsersList: LinearLayout
    private lateinit var llyEngineerList: LinearLayout
    private lateinit var llyAddOffer:LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDashBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        DbInstance.setLastLoginAs(this@AdminDashBoardActivity, Constants.admin)

        llyAnalysisServiceTakenTasks = binding.llyCheckServicesView
        llyOffersModify = binding.llyEditOffer
        llyProductsModify = binding.llyEditProduct
        llyServiceModify = binding.llyEditService
        llyPartsModify = binding.llyEditParts
        llyUsersList = binding.llyEditUsersList
        llyEngineerList = binding.llyEditEngineersList
        llyAddOffer = binding.llyAddOffer

        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(AdminDashBoradViewModel::class.java)
       // viewModel.listenforNotifications()
        llyAnalysisServiceTakenTasks.setOnClickListener {
            startActivity(Intent(this@AdminDashBoardActivity, TakenServiceCatagoryActivity::class.java)
                .putExtra("fromAdmin", true))
            moveWithAnimationToAnotherActivity()
        }
        llyOffersModify.setOnClickListener {
            startActivity(Intent(this@AdminDashBoardActivity, OfferModifyActivity::class.java))
            moveWithAnimationToAnotherActivity()
        }
        llyServiceModify.setOnClickListener {
            startActivity(Intent(this@AdminDashBoardActivity, DisplayServiceCatagoryActivity::class.java))
            moveWithAnimationToAnotherActivity()
        }
        llyProductsModify.setOnClickListener {
            startActivity(Intent(this@AdminDashBoardActivity, BuyOurProductsCatagoryDisplayActivity::class.java))
            moveWithAnimationToAnotherActivity()
        }

        llyPartsModify.setOnClickListener {
            startActivity(Intent(this@AdminDashBoardActivity, DisplayPartsActivity::class.java))
            moveWithAnimationToAnotherActivity()
        }

        llyEngineerList.setOnClickListener {
            startActivity(Intent(this@AdminDashBoardActivity, UsersAndEngineersListActivity::class.java)
                .putExtra(Constants.userType, Constants.UsersType.engineer.name))
            moveWithAnimationToAnotherActivity()
        }

        llyUsersList.setOnClickListener {
            startActivity(Intent(this@AdminDashBoardActivity, UsersAndEngineersListActivity::class.java)
                .putExtra(Constants.userType, Constants.UsersType.user.name))
            moveWithAnimationToAnotherActivity()
        }

        llyAddOffer.setOnClickListener {
            startActivity(Intent(this@AdminDashBoardActivity, AddOfferActivity::class.java))
            moveWithAnimationToAnotherActivity()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.dash_board_menu, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_item_logout){
            viewModel.logoutuser()
            finish()
            startActivity(Intent(this@AdminDashBoardActivity, LoginActivity::class.java))
            moveWithAnimationToAnotherActivity()
        }
        return true

    }
}