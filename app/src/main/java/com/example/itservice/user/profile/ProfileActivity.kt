package com.example.itservice.user.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import coil.load
import coil.transform.CircleCropTransformation
import com.example.itservice.application.TAG
import com.example.itservice.base.BaseActivity
import com.example.itservice.common.LoginActivity
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.common.utils.DbInstance
import com.example.itservice.common.utils.DocumentUpload
import com.example.itservice.databinding.ActivityAddOfferBinding
import com.example.itservice.databinding.ActivityProfileBinding
import com.example.itservice.user.registation.UserRegistationFragment

class ProfileActivity : BaseActivity() {
    private val IMAGE_PICK_REQUEST: Int = 111
    private lateinit var binding: ActivityProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private lateinit var ivUser: ImageView
    private lateinit var tvUserName: TextView
    private lateinit var tvUserEmail: TextView
    private lateinit var tvUserTIN: TextView
    private lateinit var tvUserNID: TextView
    private lateinit var tvUserContract: TextView
    private lateinit var tvUserCompanyAddress: TextView
    private lateinit var tvUserCompanyName: TextView
    private lateinit var ivUploadImage: ImageView
    private var photoUpload: DocumentUpload? = null
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitleForActivity("Profile")
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ivUser = binding.ivUserDash1
        tvUserName = binding.tvUserProName
        tvUserEmail = binding.tvUserProEmail
        tvUserContract = binding.tvUserProPhone
        tvUserNID = binding.tvUserProNID
        tvUserTIN = binding.tvUserProTin
        tvUserCompanyName = binding.tvUserProCompanyName
        tvUserCompanyAddress = binding.tvUserProCompanyAddress
        ivUploadImage = binding.ivUploadImage
        progressBar = binding.pvPhotoUpload
        progressBar.visibility = View.GONE

        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(ProfileViewModel::class.java)
        val uid = DbInstance.getUserUid(context = this@ProfileActivity)
        viewModel.getUserInfo(uid)

        ivUploadImage.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            photoUpload = DocumentUpload(this@ProfileActivity)
            photoUpload?.selectImage(IMAGE_PICK_REQUEST)
        }

        viewModel.userInfo.observe(this){ user ->
            if(user!=null){
                var profileUrl = user.profileImage
                if(profileUrl == null){
                    profileUrl = DbInstance.getDefaultImage()
                }
                if(profileUrl!="") ivUser.load(profileUrl){
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }
                tvUserName.setText(user.fullName)
                tvUserEmail.setText(user.email)
                tvUserContract.setText(user.contactNumber)
                tvUserNID.setText("${user.nid}")
                tvUserTIN.setText(user.tin)
                tvUserCompanyName.setText(user.companyName)
                tvUserCompanyAddress.setText(user.companyAddress)
            }else{
                Toast.makeText(this@ProfileActivity, "Failed to fetch user", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.uploadPhoto.observe(this){ path->
            if(path!=""){
                val uid = DbInstance.getUserUid(this@ProfileActivity)
                DbInstance.setUserImage(this@ProfileActivity, path)
                ivUser.load(path){
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }
                viewModel.updateUserProfilePic(uid,path)
            }else{
                progressBar.visibility = View.GONE
            }
        }
        viewModel.isProfileUpdated.observe(this){
            progressBar.visibility = View.GONE
            if(it){
                Toast.makeText(this@ProfileActivity, "Photo updated", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this@ProfileActivity, "Failed to update profile", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == IMAGE_PICK_REQUEST
            && data != null
            && data.getData() != null){
            val filePath = data.getData();
            Log.d("TAG", "onActivityResult: $filePath")
            if(filePath!=null && filePath?.path?.length!! >0){
                val paths = filePath?.path?.split("/")
                val imageName = paths?.get(paths.size-1)
                Log.d(TAG, "onActivityResult: "+imageName)
                val uid = DbInstance.getUserUid(this@ProfileActivity)
                photoUpload?.uploadDocumentInFireStore(uid, filePath.toString(), viewModel.uploadPhoto)
            }else{
                progressBar.visibility = View.GONE
            }
        }else{
            progressBar.visibility = View.GONE
        }
    }
}