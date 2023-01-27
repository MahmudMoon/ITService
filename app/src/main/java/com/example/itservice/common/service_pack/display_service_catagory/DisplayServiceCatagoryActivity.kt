package com.example.itservice.common.service_pack.display_service_catagory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itservice.R
import com.example.itservice.base.BaseActivity
import com.example.itservice.common.service_pack.add_catagories.AddCatagoryActivity
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.common.models.ServiceCatagory
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.example.itservice.databinding.ActivityDisplayServiceCatagoryBinding

class DisplayServiceCatagoryActivity : BaseActivity() {
    private lateinit var binding: ActivityDisplayServiceCatagoryBinding
    private lateinit var viewModel: DisplayServiceCatagoryViewModel
    private var rvDisplayCatagory: RecyclerView? = null
    private var rvCatagoryAdapter: CatagoryDisplayAdapter? = null
    private var catList = ArrayList<ServiceCatagory>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisplayServiceCatagoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(DisplayServiceCatagoryViewModel::class.java)
        viewModel.getServiceCatagories()

        rvDisplayCatagory = binding.rvDisplayCatagory
        rvCatagoryAdapter = CatagoryDisplayAdapter(this@DisplayServiceCatagoryActivity, catList)
        rvDisplayCatagory?.layoutManager = GridLayoutManager(this, 2)
        rvDisplayCatagory?.adapter = rvCatagoryAdapter

        viewModel.catagoryList.observe(this){ list->
            catList.clear()
            catList.addAll(list)
            rvCatagoryAdapter?.notifyDataSetChanged()
        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        if(DbInstance.getLastLoginAs(this@DisplayServiceCatagoryActivity).equals(Constants.admin))
            menuInflater.inflate(R.menu.display_add_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_add_catagory){
            startActivity(Intent(this@DisplayServiceCatagoryActivity, AddCatagoryActivity::class.java))
            moveWithAnimationToAnotherActivity()
            return true
        }else return super.onOptionsItemSelected(item)
    }
}