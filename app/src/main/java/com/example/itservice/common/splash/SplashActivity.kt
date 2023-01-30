package com.example.itservice.common.splash

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.itservice.R
import com.example.itservice.admin.admin_dashboard.AdminDashBoardActivity
import com.example.itservice.application.ITApplication
import com.example.itservice.application.TAG
import com.example.itservice.base.BaseActivity
import com.example.itservice.common.LoginActivity
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.DbInstance
import com.example.itservice.databinding.ActivitySlpashBinding
import com.example.itservice.engineer.dashboard.EngineerDashBoardActivity
import com.example.itservice.user.user_dash_board.UserdashboardActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


class SplashActivity : BaseActivity() {
    private lateinit var binding: ActivitySlpashBinding

    private lateinit var viewModel: SplashViewModel
    var closeThread: Int? = null
    var alartDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySlpashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        viewModel =
            ViewModelProvider(this, ViewModelProviderFactory()).get(SplashViewModel::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            for (i in 0..100 step 2) {
                Log.d(TAG, "onCreate: running in thread + $i")
                // progressBar.progress = i
                delay(50)
                if (closeThread != null) {
                    Log.d(TAG, "onCreate: break")
                    break
                }
            }
        }.invokeOnCompletion {
            val userLogin = viewModel.userLoginData()
            if (userLogin == null) {
                closeThread = 1
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                moveWithAnimationToAnotherActivity()
            } else {
                closeThread = 1
                when(DbInstance.getLastLoginAs(this@SplashActivity)){
                    Constants.admin ->{
                        startActivity(Intent(this@SplashActivity, AdminDashBoardActivity::class.java))
                        moveWithAnimationToAnotherActivity()
                    }

                    Constants.engineer ->{
                        startActivity(Intent(this@SplashActivity, EngineerDashBoardActivity::class.java))
                        moveWithAnimationToAnotherActivity()
                    }

                    Constants.user ->{
                        startActivity(Intent(this@SplashActivity, UserdashboardActivity::class.java))
                        moveWithAnimationToAnotherActivity()
                    }

                    else ->{
                        startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                        moveWithAnimationToAnotherActivity()
                    }
                }
            }
        }

        val slideAnimationLeftToRight =
            AnimationUtils.loadAnimation(this, R.anim.side_slide_left_to_right)
        val slideAnimationRightToLeft =
            AnimationUtils.loadAnimation(this, R.anim.side_slide_right_to_left)
        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        binding.tvTitle1.startAnimation(slideAnimationLeftToRight)
        binding.tvTitle2.startAnimation(slideAnimationRightToLeft)
        binding.ivIcon.startAnimation(fadeInAnimation)


    }

    private fun displayDialog() {
        CoroutineScope(Dispatchers.Main).launch {
            alartDialog = AlertDialog.Builder(this@SplashActivity)
                .setTitle("Error")
                .setMessage("No internet found")
                .setCancelable(false)
                .setPositiveButton("Retry", positiveBtnListener)
                .setNegativeButton("Close Application", negativeBtnListener)
                .create()
            alartDialog?.show()
        }
    }

    private val positiveBtnListener: DialogInterface.OnClickListener =
        object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                startActivity(Intent(this@SplashActivity, SplashActivity::class.java))
                moveWithAnimationToAnotherActivity()
            }
        }
    private val negativeBtnListener: DialogInterface.OnClickListener =
        object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                finishAffinity()
            }
        }

    override fun onPause() {
        super.onPause()
        alartDialog?.dismiss()
    }

}
