package com.example.itservice.common.service_pack.display_service_catagory.display_brand_list

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itservice.R
import com.example.itservice.admin.service_pack.add_service.add_brandagories.add_brand.AddBrandActivity
import com.example.itservice.base.BaseActivity
import com.example.itservice.common.service_pack.display_service_catagory.display_brand_list.display_service.DisplayServiceListActivity
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.common.models.Brand
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.example.itservice.databinding.ActivityBrandListDisplayBinding

class BrandListDisplayActivity : BaseActivity() , BrandClick{
    private lateinit var binding: ActivityBrandListDisplayBinding
    private lateinit var viewModel: BrandListDisplayViewModel
    private var rvDisplayBrand: RecyclerView? = null
    private var rvBrandAdapter: BrandDisplayAdapter? = null
    private var brandList = ArrayList<Brand>()
    private lateinit var catId: String
    private lateinit var catName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBrandListDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(BrandListDisplayViewModel::class.java)

        val bundle = intent?.extras
        if(bundle!=null){
            catId = bundle.getString(Constants.CatagoryId, "")
            catName = bundle.getString(Constants.CatagoryName, "")
            viewModel.getBrandListForCatagorySelected(catId)
        }

        rvDisplayBrand = binding.rvDisplayBrandList
        rvBrandAdapter = BrandDisplayAdapter(this@BrandListDisplayActivity, brandList)
        rvDisplayBrand?.layoutManager = GridLayoutManager(this, 2)
        rvDisplayBrand?.adapter = rvBrandAdapter


        viewModel.brandData.observe(this){ data ->
            brandList.clear()
            brandList.addAll(data)
            rvBrandAdapter?.notifyDataSetChanged()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        if(DbInstance.getLastLoginAs(this@BrandListDisplayActivity).equals(Constants.admin))
             menuInflater.inflate(R.menu.display_add_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_add_catagory){
            startActivity(Intent(this@BrandListDisplayActivity, AddBrandActivity::class.java).putExtra(Constants.CatagoryId, catId))
            moveWithAnimationToAnotherActivity()
            return true
        }else return super.onOptionsItemSelected(item)
    }

    override fun onBankItemclick(brand: Brand) {
        //brandId, brandName, catId, catName
        startActivity(Intent(this, DisplayServiceListActivity::class.java)
            .putExtra(Constants.CatagoryId, catId)
            .putExtra(Constants.CatagoryName, catName)
            .putExtra(Constants.BrandId, brand.id)
            .putExtra(Constants.BrandName, brand.name))
        moveWithAnimationToAnotherActivity()

    }
}