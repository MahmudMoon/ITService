package com.example.itservice.admin.show_applications

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itservice.admin.show_applications.display_form.DiplayFormActivity
import com.example.itservice.admin.show_applications.display_form.DisplayFormViewModel
import com.example.itservice.admin.users_and_engineers_list.ListDataAdapter
import com.example.itservice.application.TAG
import com.example.itservice.base.BaseActivity
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.common.models.Engineer
import com.example.itservice.common.utils.Constants
import com.example.itservice.databinding.ActivityDisplayApplicationListBinding

class DisplayApplicationListActivity : BaseActivity(), iApplicationSelected {
    private lateinit var binding: ActivityDisplayApplicationListBinding
    private lateinit var viewModel: DisplayApplicationListViewModel
    private lateinit var rvDisplayEngs: RecyclerView
    private lateinit var adapter: ListDataAdapter
    private lateinit var list: ArrayList<Engineer>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisplayApplicationListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitleForActivity("Application list")
        list = ArrayList()
        adapter = ListDataAdapter(this@DisplayApplicationListActivity, list)
        rvDisplayEngs = binding.rvDisplayEngApplication
        rvDisplayEngs.layoutManager = LinearLayoutManager(this)
        rvDisplayEngs.adapter = adapter


        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(DisplayApplicationListViewModel::class.java)

        viewModel.getAllApplications()

        viewModel.engList.observe(this){ applicationlist ->
            if(applicationlist!=null){
                list.clear()
                list.addAll(applicationlist)
                adapter.notifyDataSetChanged()
            }else{
                Toast.makeText(this, "No application found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onItemSelected(engineer: Engineer) {
        Log.d(TAG, "onItemSelected: "+engineer.fullName)
        startActivity(Intent(this@DisplayApplicationListActivity, DiplayFormActivity::class.java).putExtra(
            Constants.engUid, engineer.uid
        ))
    }
}