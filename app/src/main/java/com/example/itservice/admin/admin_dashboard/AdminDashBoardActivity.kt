package com.example.itservice.admin.admin_dashboard

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

class AdminDashBoardActivity : BaseActivity(), iAdminItemClicked {
    private lateinit var binding: ActivityAdminDashBoardBinding
    private lateinit var viewModel: AdminDashBoradViewModel
    private lateinit var rvItems: RecyclerView
    private lateinit var adminItemAdapter: AdminListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDashBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViewModel()
        supportActionBar?.title = "Admin Home"
        DbInstance.setLastLoginAs(this@AdminDashBoardActivity, Constants.admin)

        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(AdminDashBoradViewModel::class.java)
        val items = viewModel.getDashBoardItems()
        adminItemAdapter = AdminListAdapter(this@AdminDashBoardActivity, items)

        rvItems = binding.rvAdminItems
        rvItems.layoutManager = GridLayoutManager(this@AdminDashBoardActivity, 2)
        rvItems.adapter = adminItemAdapter


       // viewModel.listenforNotifications()
//        llyAnalysisServiceTakenTasks.setOnClickListener {
//
//        }
//        llyOffersModify.setOnClickListener {
//
//            moveWithAnimationToAnotherActivity()
//        }
//        llyServiceModify.setOnClickListener {
//
//            moveWithAnimationToAnotherActivity()
//        }
//        llyProductsModify.setOnClickListener {
//
//        }
//
//        llyPartsModify.setOnClickListener {
//            moveWithAnimationToAnotherActivity()
//        }
//
//        llyEngineerList.setOnClickListener {
//
//            moveWithAnimationToAnotherActivity()
//        }
//
//        llyUsersList.setOnClickListener {
//
//            moveWithAnimationToAnotherActivity()
//        }
//
//        llyAddOffer.setOnClickListener {
//
//            moveWithAnimationToAnotherActivity()
//        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    override fun adminItemClickedAt(position: Int) {
        when(position){
            0 -> {
                startActivity(Intent(this@AdminDashBoardActivity, TakenServiceCatagoryActivity::class.java)
                .putExtra("fromAdmin", true))
            }
            1 -> {
                startActivity(Intent(this@AdminDashBoardActivity, BuyOurProductsCatagoryDisplayActivity::class.java))
            }
            2 -> {
                startActivity(Intent(this@AdminDashBoardActivity, DisplayServiceCatagoryActivity::class.java))
            }
            3 -> {
                startActivity(Intent(this@AdminDashBoardActivity, DisplayPartsActivity::class.java))
            }
            4 -> {
                startActivity(Intent(this@AdminDashBoardActivity, AddOfferActivity::class.java))
            }
            5 -> {
                startActivity(Intent(this@AdminDashBoardActivity, UsersAndEngineersListActivity::class.java)
                .putExtra(Constants.userType, Constants.UsersType.user.name))
            }
            6 -> {
                startActivity(Intent(this@AdminDashBoardActivity, UsersAndEngineersListActivity::class.java)
                .putExtra(Constants.userType, Constants.UsersType.engineer.name))
            }
            7 -> {
                startActivity(Intent(this@AdminDashBoardActivity, OfferModifyActivity::class.java))
            }
            8 -> {
                // need to change this one
                startActivity(Intent(this@AdminDashBoardActivity, OfferModifyActivity::class.java))
            }
        }
        moveWithAnimationToAnotherActivity()
    }
}