package com.example.itservice.common

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.get
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.itservice.R
import com.example.itservice.base.BaseActivity
import com.example.itservice.databinding.ActivityLoginBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var navController: NavController
    lateinit var navView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        navView = binding.bottomNavView
        navView.setupWithNavController(navController)
    }

    fun selectTabAt(index: Int){
        if(!navView.menu[index].isChecked){
            navView.menu.get(index).setChecked(true)
        }
    }

    override fun onBackPressed() {
        if(!navController.popBackStack()){
            finishAffinity()
        }
    }

    fun setTitle(title: String){
        supportActionBar?.title = title
    }
}