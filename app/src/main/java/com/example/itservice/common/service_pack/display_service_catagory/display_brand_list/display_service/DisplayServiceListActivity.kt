package com.example.itservice.common.service_pack.display_service_catagory.display_brand_list.display_service

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itservice.R
import com.example.itservice.admin.service_pack.display_service_catagory.display_service_list.display_service.DisplayServiceListViewModel
import com.example.itservice.base.BaseActivity
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.common.models.Service
import com.example.itservice.common.service_pack.add_catagories.add_brand.add_service.AddServiceActivity
import com.example.itservice.common.service_pack.display_service_catagory.display_brand_list.display_service.show_service.ShowServiceActivity
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.example.itservice.databinding.ActivityDisplayServiceListBinding

class DisplayServiceListActivity : BaseActivity(), ClickOnService {
    private lateinit var binding: ActivityDisplayServiceListBinding
    private lateinit var viewModel: DisplayServiceListViewModel

    private var rvDisplayService: RecyclerView? = null
    private var rvServiceAdapter: ServiceDisplayAdapter? = null
    private var serviceList = ArrayList<Service>()
    private lateinit var catId: String
    private lateinit var brandId: String
    private lateinit var catName: String
    private lateinit var brandName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitleForActivity("Lists")
        binding = ActivityDisplayServiceListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(
            DisplayServiceListViewModel::class.java)

        val bundle = intent?.extras
        if(bundle!=null){
            catId = bundle.getString(Constants.CatagoryId, "")
            brandId = bundle.getString(Constants.BrandId, "")
            catName = bundle.getString(Constants.CatagoryName, "")
            brandName = bundle.getString(Constants.BrandName, "")

            viewModel.getserviceListForCatagorySelected(catId, brandId)
        }

        rvDisplayService = binding.rvDisplayServiceList
        rvServiceAdapter = ServiceDisplayAdapter(this@DisplayServiceListActivity, serviceList)
        rvDisplayService?.layoutManager = GridLayoutManager(this, 2)
        rvDisplayService?.adapter = rvServiceAdapter


        viewModel.serviceData.observe(this){ data ->
            serviceList.clear()
            serviceList.addAll(data)
            rvServiceAdapter?.notifyDataSetChanged()
        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        if(DbInstance.getLastLoginAs(this@DisplayServiceListActivity).equals(Constants.admin))
             menuInflater.inflate(R.menu.display_add_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_add_catagory){
            startActivity(Intent(this@DisplayServiceListActivity, AddServiceActivity::class.java)
                .putExtra(Constants.CatagoryId, catId)
                .putExtra(Constants.CatagoryName, catName)
                .putExtra(Constants.BrandId, brandId)
                .putExtra(Constants.BrandName, brandName)
            )
            moveWithAnimationToAnotherActivity()
            return true
        }else return super.onOptionsItemSelected(item)
    }

    override fun onServiceItemClicked(service: Service) {
         startActivity(Intent(this@DisplayServiceListActivity, ShowServiceActivity::class.java)
             .putExtra(Constants.CatagoryId, catId )
             .putExtra(Constants.BrandId, brandId)
             .putExtra(Constants.ServiceId, service.id))
        moveWithAnimationToAnotherActivity()
    }
}