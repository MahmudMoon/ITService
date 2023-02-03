package com.example.itservice.common

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.get
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.itservice.R
import com.example.itservice.application.TAG
import com.example.itservice.base.BaseActivity
import com.example.itservice.databinding.ActivityLoginBinding
import com.example.itservice.user.registation.UserRegistationFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var navController: NavController
    lateinit var navView: BottomNavigationView
    var filePath: Uri? = null
    lateinit var navHostFragment: NavHostFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navHostFragment =
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 109
            && data != null
            && data.getData() != null){
            filePath = data.getData();
            Log.d("TAG", "onActivityResult: $filePath")
            if(filePath!=null && filePath?.path?.length!! >0){
                val paths = filePath?.path?.split("/")
                val imageName = paths?.get(paths.size-1)
                Log.d(TAG, "onActivityResult: "+imageName)
                val framgent = navHostFragment.childFragmentManager.fragments.get(0)
                if(framgent is UserRegistationFragment){
                    framgent.onImagePicked(filePath, imageName)
                }
            }
        }
    }

}