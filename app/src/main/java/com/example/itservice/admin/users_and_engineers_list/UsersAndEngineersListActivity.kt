package com.example.itservice.admin.users_and_engineers_list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itservice.R
import com.example.itservice.base.BaseActivity
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.common.utils.Constants
import com.example.itservice.databinding.ActivityUsersAndEngineersListBinding

class UsersAndEngineersListActivity : BaseActivity() {
    private lateinit var binding: ActivityUsersAndEngineersListBinding
    private lateinit var viewModel: UserAndEngineersListViewModel
    private lateinit var rvDisplayList: RecyclerView
    private lateinit var adapter: ListDataAdapter
    private var listValue = ArrayList<Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsersAndEngineersListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        rvDisplayList = binding.rvDisplayListData
        rvDisplayList.layoutManager = LinearLayoutManager(this)
        adapter = ListDataAdapter(this, listValue)
        rvDisplayList.adapter = adapter

        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(UserAndEngineersListViewModel::class.java)

        val bundle = intent?.extras
        if(bundle != null){
           val type = bundle.getString(Constants.userType, "")
            if(type.equals(Constants.UsersType.user.name)){
                viewModel.getUsersList()
                setTitleForActivity("Users")
            }else if (type.equals(Constants.UsersType.engineer.name)){
                viewModel.getEngineersList()
                setTitleForActivity("Engineers")
            }
        }

        viewModel.usersData.observe(this){
            listValue.clear()
            listValue.addAll(it)
            adapter.notifyDataSetChanged()
        }

        viewModel.engsData.observe(this){
            listValue.clear()
            listValue.addAll(it)
            adapter.notifyDataSetChanged()
        }
    }

}