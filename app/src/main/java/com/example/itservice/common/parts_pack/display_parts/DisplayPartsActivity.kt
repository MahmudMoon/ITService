package com.example.itservice.common.display_parts

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.itservice.R
import com.example.itservice.application.TAG
import com.example.itservice.base.BaseActivity
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.common.parts_pack.add_part.AddPartActivity
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.example.itservice.databinding.ActivityDisplayPartsBinding
import kotlin.math.log

class DisplayPartsActivity : BaseActivity(), PartUpdateClicked {
    private lateinit var binding: ActivityDisplayPartsBinding
    private lateinit var viewModel: DisplayPartViewModel
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisplayPartsBinding.inflate(layoutInflater)
        setTitleForActivity("Display parts")
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(DisplayPartViewModel::class.java)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerPartsView) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        if(DbInstance.getLastLoginAs(this@DisplayPartsActivity).equals(Constants.admin))
            menuInflater.inflate(R.menu.display_add_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_add_catagory){
            startActivity(Intent(this@DisplayPartsActivity, AddPartActivity::class.java))
            moveWithAnimationToAnotherActivity()
            return true
        }else return super.onOptionsItemSelected(item)
    }

    override fun onPartUpdate(partID: String) {
        Log.d(TAG, "onPartUpdate: part id: ${partID}")
        startActivity(Intent(this@DisplayPartsActivity, AddPartActivity::class.java)
            .putExtra(Constants.partID, partID))
        moveWithAnimationToAnotherActivity()
    }

}

interface PartUpdateClicked{
    fun onPartUpdate(partID: String)
}