package com.example.itservice.user.registation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.ViewModelProvider
import com.example.itservice.R
import com.example.itservice.application.TAG
import com.example.itservice.base.BaseFragment
import com.example.itservice.common.LoginActivity
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.ContextExtentions
import com.example.itservice.common.utils.DbInstance
import com.example.itservice.common.utils.DocumentUpload
import com.example.itservice.databinding.FragmentUserRegistationBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserRegistationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

interface userRegistrationImagePicked{
    fun onImagePicked(filePath: Uri?, imageName: String?)
}

class UserRegistationFragment : BaseFragment(), TextWatcher, userRegistrationImagePicked {
    private val IMAGE_PICK_REQUEST: Int = 109

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentUserRegistationBinding
    private lateinit var tvUserSignIn: TextView
    private lateinit var viewModel: UserRegistationViewModel
    private lateinit var btnSignUp: Button
    private var etuserName: AppCompatEditText? = null
    private var etuserEmail: AppCompatEditText? = null
    private var etuserPassword: AppCompatEditText? = null
    private var etuserCompanyName: AppCompatEditText? = null
    private var etuserCompanyAddress: AppCompatEditText? = null
    private var etuserTIN: AppCompatEditText? = null
    private var etuserNID: AppCompatEditText? = null
    private var etuserContactNumber: AppCompatEditText? = null
    private lateinit var btnSelectImage: Button
    private var userName: String? = null
    private var userEmail: String? = null
    private var userPassword: String? = null
    private var userCompanyName: String? = null
    private var userCompanyAddress: String? = null
    private var userTIN: String? = null
    private var userContactNumber: String? = null
    private var userNID: String? = null
    private var progressBar: ProgressBar? = null
    private var tvImageName: TextView? = null
    private var photoUpload: DocumentUpload? = null
    private var imageName: String? =null
    var filePath: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserRegistationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as LoginActivity).selectTabAt(0)
        viewModel = ViewModelProvider(
            this,
            ViewModelProviderFactory()
        ).get(UserRegistationViewModel::class.java)
        tvUserSignIn = binding.tvSignUpUserRegistation
        btnSignUp = binding.btnSingUpuserRegistation
        etuserName = binding.etFullnameUserRegistation
        etuserEmail = binding.etEmailUserRegistation
        etuserPassword = binding.etPasswordUserRegistation
        etuserCompanyName = binding.etCompanyNameUserRegistation
        etuserCompanyAddress = binding.etCompanyAddressUserRegistation
        etuserTIN = binding.etTinUserRegistation
        etuserContactNumber = binding.etContactNumberUserRegistation
        etuserNID = binding.etNidUserRegistation
        progressBar = binding.pbUserRegistration
        btnSelectImage = binding.btnSelectImageAddUser
        tvImageName = binding.tvSelectImageAddUser
        tvImageName?.keyListener = null
        (requireActivity() as LoginActivity).setTitle("User registration")

        btnSelectImage.setOnClickListener {
            // pick an image
            tvImageName?.setError(null)
            //pick an image
            photoUpload = DocumentUpload(this@UserRegistationFragment.requireActivity() as LoginActivity)
            photoUpload?.selectImage(IMAGE_PICK_REQUEST)
        }

        btnSignUp.setOnClickListener {
            ContextExtentions.hideKeyboard(it, requireContext())
            userName = setErrorMessage(etuserName, "Enter Valid Name")
            userEmail = setErrorMessage(etuserEmail, "Enter Valid email")
            userPassword = setErrorMessage(etuserPassword, "Valid Password must be more than 7 characters")
            userCompanyName = setErrorMessage(etuserCompanyName, "Enter valid company Name")
            userCompanyAddress = setErrorMessage(etuserCompanyAddress, "Enter valid company Address")
            userTIN = etuserTIN?.text.toString()
            userContactNumber = setErrorMessage(etuserContactNumber, "Enter valid contact number")
            userNID = setErrorMessage(etuserNID, "Enter valid NID")
            imageName = setErrorMessage(tvImageName, "Please select an image")

            if (userName != "" && userEmail != "" && filePath != null
                && userPassword != "" && userCompanyName != "" && userCompanyAddress != null
                && userContactNumber != "" && userNID != ""
            ) {
                progressBar?.visibility = View.VISIBLE
                viewModel.registerUserWithEmailPassword(
                    userName!!,
                    userEmail!!,
                    userPassword!!,
                    userTIN!!,
                    userNID!!,
                    userCompanyName!!,
                    userCompanyAddress!!,
                    userContactNumber!!)
            }
        }

        viewModel.userAuthResult.observe(viewLifecycleOwner){
            if(it.isSuccess){
                val uid = it.resultData as String
                DbInstance.setUserUid(requireContext(), uid)
                photoUpload?.uploadDocumentInFireStore(uid, filePath.toString(), viewModel.uploadPhoto)
            }else{
                progressBar?.visibility = View.GONE
                Toast.makeText(requireContext(), "Registration failed", Toast.LENGTH_SHORT).show()
            }
        }
        
        tvUserSignIn.setOnClickListener {
            try {
                (requireActivity() as LoginActivity).navController.navigate(R.id.action_userRegistationFragment_to_userLoginFragment)
            } catch (e: Exception) {
                Log.e(TAG, "onViewCreated: ${e.localizedMessage}")
            }
        }

        viewModel.uploadPhoto.observe(viewLifecycleOwner){
            val path = it
            viewModel.updateProfileImage(path)
            Log.d(TAG, "onViewCreated: USER IMAGE AT "+ path)
        }

        viewModel.isProfileImageUpdated.observe(viewLifecycleOwner){
            progressBar?.visibility = View.GONE
            if(it){
                Toast.makeText(requireContext(), "Registration successful", Toast.LENGTH_SHORT).show()
                requireActivity().startActivity(Intent(requireContext(), LoginActivity::class.java)
                    .putExtra(Constants.email, userEmail)
                    .putExtra(Constants.password, userPassword)
                    .putExtra(Constants.tabSelection, 0))
                moveWithAnimationToAnotherActivity()
            }else{
                Toast.makeText(requireContext(), "Registration failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun setErrorMessage(view: TextView?, message: String): String? {
        try {
            val data = view?.text.toString()
            if (data == "" ) {
                view?.error = message
                return data
            }else if(message == "Valid Password must be more than 7 characters" && data.length<=7){
                view?.error = message
                return ""
            }
            return data
        } catch (exception: Exception) {
            view?.error = message
        }
        return null
    }

    fun cleanErrorMessage(view: TextView?) {
        try {
            view?.error = null
        } catch (exception: Exception) {
            view?.error = null
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun afterTextChanged(p0: Editable?) {
        when (p0.hashCode()) {
            etuserName?.text.hashCode() -> cleanErrorMessage(etuserName)
            etuserEmail?.text.hashCode() -> cleanErrorMessage(etuserEmail)
            etuserPassword?.text.hashCode() -> cleanErrorMessage(etuserPassword)
            etuserCompanyName?.text.hashCode() -> cleanErrorMessage(etuserCompanyName)
            etuserCompanyAddress?.text.hashCode() -> cleanErrorMessage(etuserCompanyAddress)
            etuserTIN?.text.hashCode() -> cleanErrorMessage(etuserTIN)
            etuserContactNumber?.text.hashCode() -> cleanErrorMessage(etuserContactNumber)
            etuserNID?.text.hashCode() -> cleanErrorMessage(etuserNID)
            tvImageName?.text.hashCode() -> cleanErrorMessage(tvImageName)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserRegistationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserRegistationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onImagePicked(filePath: Uri?, imageName: String?) {
        Log.d(TAG, "onImagePicked: u_r "+ filePath + "\n" + imageName)
        if(filePath!=null && imageName != null){
            this.filePath = filePath
            this.imageName = imageName
            tvImageName?.text = imageName
        }
    }
}