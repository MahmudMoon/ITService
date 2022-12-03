package com.example.itservice.user.user_dash_board

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import com.example.itservice.R
import com.example.itservice.common.LoginActivity
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.Constants.CHANNEL_ID
import com.example.itservice.common.utils.DbInstance
import com.example.itservice.databinding.ActivityUserdashboardBinding
import com.example.itservice.user.ask_service_catagory.UserServiceActivity

class UserdashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserdashboardBinding
    private lateinit var viewModel: UserdashboardViewModel
    private lateinit var llyBuyOurProducts: LinearLayout
    private lateinit var llyTakeOurService: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserdashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.admin)
            .setContentTitle("My notification")
            .setContentText("Much longer text that cannot fit one line...")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Much longer text that cannot fit one line..."))
            .setPriority(NotificationCompat.PRIORITY_HIGH)

       val notificationManagerCompat =  NotificationManagerCompat.from(this)
        notificationManagerCompat.notify(123, builder.build())

        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(UserdashboardViewModel::class.java)
        DbInstance.setLastLoginAs(this@UserdashboardActivity, Constants.user)

        llyBuyOurProducts = binding.llyBuyOutProducts
        llyTakeOurService = binding.llyTakeOutService

        llyBuyOurProducts.setOnClickListener {

        }

        llyTakeOurService.setOnClickListener {
            val intent = Intent(this@UserdashboardActivity, UserServiceActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.dash_board_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_item_logout){
            viewModel.logoutuser()
            finish()
            startActivity(Intent(this@UserdashboardActivity, LoginActivity::class.java))
        }
        return true
    }
}